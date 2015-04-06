package org.home.garbage

/**
 * Created by mate.magyari on 06/08/2014.
 */
object DuckTyping extends App {

  class Number1 {
    def number() = 1
  }

  class Number2 {
    def number() = 2
  }

  def readNumber(a: {def number(): Int}) = a.number()

  assert(1 == readNumber(new Number1()))
  assert(2 == readNumber(new Number2()))


  trait Closeable[R] {
    def close(r: R): Unit
  }

  object Closeable {

    implicit object SqlConnectionIsCloseable extends Closeable[String] {
      def close(conn: String) = conn + conn
    }

  }

  def withResource[R: Closeable](r: R) = r


  trait Named[R] {
    def name(r: R): String
  }

  class Num {}

  object Named {

    implicit object Num extends Named[String] {
      def name(r: String) = r
    }

  }

  def nameReader[R: Named](r: R) = implicitly[Named[R]].name(r)

  //assert(nameReader(new Num()) == "a")
  //assert(new Num().name("a") == "a")

}
