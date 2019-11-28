package org.home.threads

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.scalalogging.StrictLogging

import scala.collection.immutable.Seq
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random
import ExecutionContextFactory.instantiate

object SeparateThreadPoolsExperiment extends App with StrictLogging {

  val readContext: ExecutionContext = instantiate(fixedThreadPoolSize = 2)
  val writeContext: ExecutionContext = instantiate(fixedThreadPoolSize = 2)

  ExperimentRunner.run(writeContext, readContext)

}

object CommonThreadPoolExperiment extends App with StrictLogging {

  val commonContext: ExecutionContext = instantiate(fixedThreadPoolSize = 4)

  ExperimentRunner.run(writeContext = commonContext,
                       readContext = commonContext)
}

object ExperimentRunner extends StrictLogging {

  //Runs reader and writer processes with the execution contexts and logs their throughput
  def run(writeContext: ExecutionContext, readContext: ExecutionContext) = {

    def longJob(n: Int): Long = {
      //do something CPU intensive
      def now() = System.currentTimeMillis()
      val start = now()
      (1 to n).map(_ ⇒ Random.nextInt).sorted
      now() - start
    }

    def doRead(): Long = longJob(5000)
    def doWrite(): Long = longJob(25000)

    val writers = new Processes(n = 10000, operation = doWrite, writeContext)
    val readers = new Processes(n = 10000, operation = doRead, readContext)

    //start writers asynchronously
    Future(writers.run())(writeContext)
    //start readers asynchronously
    Future(readers.run())(readContext)

    //Log the throughput once in every second
    def avg(ns: Seq[Long]) = ns.sum.toDouble / ns.size

    val i = new AtomicInteger()
    def log(p: Processes): String =
      s"completed jobs [${p.jobsDone()}] avg response times [${avg(p.responseTimes())}]"
    while (i.getAndIncrement() < 100) {
      logger.info(s"""
           |iteration: ${i.get()}
           |readers: ${log(readers)}]
           |writers: ${log(writers)}]""".stripMargin)
      Thread.sleep(1000)
    }
  }
}

//Asynchronously runs 'n' processes and keeps track of their state
class Processes(n: Int, operation: () ⇒ Long, ec: ExecutionContext)
    extends StrictLogging {

  private val responseTimesBuffer: ListBuffer[Long] = ListBuffer.empty
  private val jobs = new AtomicInteger(0)

  def run(): Unit = {
    (1 to n).foreach { id ⇒
      Future {
        responseTimesBuffer.append(operation())
        jobs.incrementAndGet()
      }(ec)
    }
  }

  //only take the last 100
  def responseTimes(): Seq[Long] = responseTimesBuffer.takeRight(100).toList

  def jobsDone(): Int = jobs.get()

}

object ExecutionContextFactory extends StrictLogging {

  //Creates an ExecutionContext with a fixed thread pool
  def instantiate(fixedThreadPoolSize: Int): ExecutionContext =
    new ExecutionContext {

      val threadPool = Executors.newFixedThreadPool(fixedThreadPoolSize)

      override def reportFailure(cause: Throwable): Unit = {
        logger.error("Some error", cause)
      }

      override def execute(runnable: Runnable): Unit = {
        threadPool.submit(runnable)
      }
    }

}
