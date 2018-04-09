package org.home

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import com.typesafe.scalalogging.StrictLogging

import scala.collection.immutable.Seq
import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Random

object ThreadPools extends App with StrictLogging {

  import ExecutionContextFactory.instantiate

  val readContext: ExecutionContext = instantiate(2)
  val writeContext: ExecutionContext = instantiate(2)

//    val commonPool: ExecutionContext = instantiate(4)
//    val readContext: ExecutionContext = commonPool
//    val writeContext: ExecutionContext = commonPool

  ExperimentRunner.run(writeContext, readContext)

}

object ExperimentRunner extends StrictLogging {

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

    def avg(ns: Seq[Long]) = ns.sum.toDouble / ns.size

    val i = new AtomicInteger()
    while (i.getAndIncrement() < 100) {
      logger.info(
        s"""
           |iteration: ${i.get()}
           |readers: [${readers.jobsDone()}] [${avg(readers.responseTimes())}]
           |writers: [${writers.jobsDone()}][${avg(writers.responseTimes())}]"""
            .stripMargin)
      Thread.sleep(1000)
    }
  }
}


class Processes(n: Int, operation: () ⇒ Long, ec: ExecutionContext) extends StrictLogging {

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

  def instantiate(threadPoolSize: Int): ExecutionContext = new ExecutionContext {

    val threadPool = Executors.newFixedThreadPool(threadPoolSize)

    override def reportFailure(cause: Throwable): Unit = {
      logger.error("Some error", cause)
    }

    override def execute(runnable: Runnable): Unit = {
      threadPool.submit(runnable)
    }
  }

}
