package org.home.garbage

/**
 * Created by mate.magyari on 28/07/2014.
 */
object MyStudy extends App {

  //fib
  def fib(n: Int): Int =
    if (n < 2) 1
    else fib(n - 1) + fib(n - 2)

  def fib2(n:Int) = {
    def loop(x:Int) = {

    }
    loop(n)
  }

  assert(fib(0) == 1)
  assert(fib(1) == 1)
  assert(fib(2) == 2)
  assert(fib(3) == 3)

  def isSorted[A](as: List[A], gt: (A, A) => Boolean): Boolean = as match {
    case Nil => true
    case head :: Nil => true
    case x :: y :: rest => gt(x, y) && isSorted(y :: rest, gt)
  }

  assert(isSorted(List(), (a: Int, b: Int) => a < b))
  assert(isSorted(List(2), (a: Int, b: Int) => a < b))
  assert(isSorted(List(3, 2), (a: Int, b: Int) => a < b) == false)
  assert(isSorted(List(2, 4, 9), (a: Int, b: Int) => a < b))
  assert(isSorted(List(5, 4, 9), (a: Int, b: Int) => a < b) == false)
  assert(isSorted(List(5, 9, 8), (a: Int, b: Int) => a < b) == false)

  //currying

  def curry[A, B, C](f: (A, B) => C): A => (B => C) = (a: A) => (b: B) => f(a, b)

  def stringDoubler(a: Boolean, b: Integer) = if (a) (2 * b).toString else b.toString

  def bc(b: Integer, c: String) = b.toString

  //A=Boolean, B=Integer, C=String

  assert(curry(stringDoubler)(true)(3) == "6")
  assert(curry(stringDoubler)(false)(3) == "3")

  def multiplierCurry(multiplier: Int): Int => Int = (x: Int) => multiplier * x

  assert(45 == multiplierCurry(9)(5))

  def uncurry[A, B, C](f: A => B => C): (A, B) => C = (a: A, b: B) => f(a)(b)

  val stringDoubler2 = uncurry(curry(stringDoubler))

  assert(stringDoubler2(true, 3) == stringDoubler(true, 3))

  // functions as values

  val even = new Function1[Int, Boolean] {
    def apply(a: Int) = if (a % 2 == 0) true else false
  }

  assert(true == even(8))
  assert(true == even.apply(8))
  assert(false == even(7))

  //partial

  def partial1[A, B, C](a: A, f: (A, B) => C): B => C = (b: B) => f(a, b)

  def f2(a: Boolean, b: Integer) = if (a) (2 * b).toString else b.toString

  def bc2(b: Integer, c: String) = b.toString

  val doubler = partial1(true, f2)
  val simpler = partial1(false, f2)
  assert(doubler(6) == "12")
  assert(simpler(6) == "6")

  //compose
  def compose[A, B, C](f: A => B, g: B => C): A => C = (a: A) => (g(f(a)))

  def zeroOrOne(a: Boolean) = if (a) 1 else 0

  def doubler2(x: Int) = (2 * x).toString

  assert(compose(zeroOrOne, doubler2)(true) == "2")
  assert(compose(zeroOrOne, doubler2)(false) == "0")

  //implement 'tail'
  //def tail[T](as: List[T]) = if (as.length < 2) Nil else tail()

}
