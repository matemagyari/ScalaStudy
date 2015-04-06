import java.io.File

import scala.io.Source

/**
 * Created by mate.magyari on 24/11/2014.
 */
object LineCounter extends App {

  def recursiveListFiles(f: File): Array[File] = {
    val these = f.listFiles
    these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
  }
  def locInFile(f:File) = Source.fromFile(f).getLines().size
  def getJavaFilesUnder(rootDir:String) = recursiveListFiles(new File(rootDir)).filter(_.getName.endsWith(".java"))
  def locInProject(root:String) = (root.replaceAll(rootDir,""), getJavaFilesUnder(root).map(f => locInFile(f)).foldLeft(0)(_+_))

  val rootDir = "/Users/mate.magyari/IdeaProjects/gamesys/gamesplatform/"

  val playerJourneySystems = List("poker-player-profile-system","poker-player-reputation-system"
    ,"poker-buddies","poker-notification-system","poker-fulfilment-system","poker-academy-system","poker-promotion-system"
    ,"gamification-system/poker-stats-system")

  //val systems = playerJourneySystems :+ "poker-game-system"
  val systems = playerJourneySystems ++ List("poker-game-system","poker-cashier-system","poker-tournament")


  val results = systems.map(s => rootDir + s).map(s => locInProject(s))

  val sumLoc = results.foldLeft(0)(_ + _._2)
  results.foreach( (r => println(r._1 + " " + r._2)))
  println("Sum is " + sumLoc)


}
