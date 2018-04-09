package org.home.fpvsoop

object GeometryOOP {

  //adding/deleting a function - change under each subtype
  //adding/deleting a subtype - no change in existing code
  sealed trait Data {
    def intersect(other: Data): Boolean
    def show(): String
  }

  case class Point(x: Double, y: Double) extends Data {
    override def intersect(other: Data): Boolean = true
    override def show(): String = "Point"
  }

  case class Line(x: Double, y: Double) extends Data {
    override def intersect(other: Data): Boolean = true
    override def show(): String = "Line"
  }

}

object GeometryFP {

  //adding/deleting a function - no change in existing code
  //adding/deleting a subtype - no change in existing code


  sealed trait Data
  case class Point(x: Double, y: Double) extends Data
  case class Line(x: Double, y: Double) extends Data

  def intersect(data: Data, other: Data): Boolean = data match {
    case d: Point ⇒ true
    case d: Line ⇒ true
  }

  def show(data: Data, other: Data): String = data match {
    case d: Point ⇒ "Point"
    case d: Line ⇒ "Line"
  }
}

object GeometryFPAndTypeClasses {

  //adding/deleting a function - no change in existing code
  //adding/deleting a subtype - change under each function

  sealed trait Data
  case class Point(x: Double, y: Double) extends Data
  case class Line(p1: Point, p2: Point) extends Data

  trait Intersectable[T] {
    def intersect(other: Data): Boolean = true
  }

  trait Showable[T] {
    def show(): String
  }

  object Typeclasses {
    implicit class ShowablePoint(p: Point) extends Showable[Point] {
      override def show(): String = "Point"
    }

    implicit class ShowableLine(p: Line) extends Showable[Line] {
      override def show(): String = "Line"
    }

    implicit class IntersectablePoint(p: Point) extends Intersectable[Point] {
      override def intersect(obj: Data): Boolean = true
    }

    implicit class IntersectableLine(p: Line) extends Intersectable[Line] {
      override def intersect(obj: Data): Boolean = true
    }
  }

  val p = Point(1, 2)
  val l = Line(Point(1, 2), Point(3, 2))

  import Typeclasses._
  p.show()
  l.show()
  p.intersect(l)
  l.intersect(p)

}