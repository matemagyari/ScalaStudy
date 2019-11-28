package org.home.fpvsoop.spaceships.oop

sealed trait Team
object Team {
  case object Red extends Team
  case object Blue extends Team
}

case class Coordinates(x: Int, y: Int)

trait World {
  def fireAt(shipId: Int, blast: Int): Unit
  def ships: Map[Int, Coordinates]
}

class DefaultWorld() extends World {
  override def fireAt(shipId: Int, blast: Int): Unit = ???

  override def ships: Map[Int, Coordinates] = ???
}

trait Ship {
  def id: Int
  def team: Team
  def hit(damage: Int): Unit
  def coordinates: Coordinates
  def move(): Unit
}

//class DefaultShip(
//    override val id: Int,
//    override val team: Team,
//    initialArmor: Int,
//    initialHealthPoint: Int,
//    initialCoordinates: Coordinates,
//    world: World) extends Ship {
//
//  private var armor: Int = initialArmor
//  private var healthPoint: Int = initialHealthPoint
//  //override var coordinates: Coordinates = initialCoordinates
//
//  override def hit(damage: Int): Unit = {
//    val effectiveHit = damage - armor
//    if (effectiveHit > 0) {
//      healthPoint -= effectiveHit
//    }
//  }
//
//  override def move(): Unit = {
//    //todo
//  }
//}


