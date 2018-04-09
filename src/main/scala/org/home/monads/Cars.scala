package org.home.monads

import org.scalatest.Matchers

import scala.collection.immutable.Seq
import scala.util.{Failure, Success, Try}

case class Vrm(value: String)

case class Car(vrm: Vrm, price: Long)

class CarNotFoundException(vrm: Vrm) extends RuntimeException

class CarStore2(private val cars: Seq[Car]) {

  def priceDifference(vrm1: Vrm, vrm2: Vrm): Try[Long] = {
    cars.find(_.vrm == vrm1)
        .fold[Try[Long]](Failure(new CarNotFoundException(vrm1))) { car1 =>
      cars.find(_.vrm == vrm2)
          .fold[Try[Long]](Failure(new CarNotFoundException(vrm2))) { car2 =>
        Success(difference(car1.price, car2.price))
      }
    }
  }

  private def difference(price1: Long, price2: Long) = price1 - price2
}

sealed trait Error
case class CarNotFound(vrm: Vrm) extends Error

class CarStore(private val cars: Seq[Car]) {

  def priceDifference(vrm1: Vrm, vrm2: Vrm): Either[Error, Long] =
    for {
      p1 ← priceOf(vrm1)
      p2 ← priceOf(vrm2)
    } yield difference(p1, p2)

  private def priceOf(vrm: Vrm): Either[Error, Long] = cars
      .find(_.vrm == vrm)
      .map(c ⇒ Right(c.price))
      .getOrElse(Left(CarNotFound(vrm)))

  private def difference(price1: Long, price2: Long) = price1 - price2
}

object CarsRunner extends App {

  val vrm1 = Vrm("vrm1")
  val vrm2 = Vrm("vrm2")
  val cs = new CarStore(Seq(Car(vrm1, 20), Car(vrm2, 30)))

  println(cs.priceDifference(vrm1, vrm2))
  println(cs.priceDifference(vrm1, Vrm("no")))

}


object Monad2 {
  def unhappy[A, B](unhappyValue: A): Monad2[A, B] =
    Monad2[A, B](unhappyValue, null.asInstanceOf[B])

  def happy[A, B](happyValue: B): Monad2[A, B] =
    Monad2(null.asInstanceOf[A], happyValue)
}

case class Monad2[A, B] private(
    private val unhappyValue: A,
    private val happyValue: B) {
  
  //one of them must be non-null, the other must be null
  assert(Set[Any](unhappyValue, happyValue).filter(_ == null).size == 1)

  def map[C](f: B ⇒ C): Monad2[A, C] = {
    if (happyValue != null)
      Monad2.happy[A, C](f(happyValue))
    else
      Monad2.unhappy[A, C](unhappyValue)
  }

  def flatMap[C](f: B ⇒ Monad2[A, C]): Monad2[A, C] = {
    if (happyValue != null)
      f(happyValue)
    else
      Monad2.unhappy[A, C](unhappyValue)
  }

}

sealed trait Monad[A, B] {
  def map[C](f: B ⇒ C): Monad[A, C]
  def flatMap[C](f: B ⇒ Monad[A, C]): Monad[A, C]
}
case class Unhappy[A, B](value: A) extends Monad[A, B] {
  override def map[C](f: B ⇒ C) = Unhappy[A, C](value)
  override def flatMap[C](f: B ⇒ Monad[A, C]) = Unhappy[A, C](value)
}
case class Happy[A, B](value: B) extends Monad[A, B] {
  override def map[C](f: B ⇒ C) = Happy[A, C](f(value))
  override def flatMap[C](f: B ⇒ Monad[A, C]) = f(value)
}

object MonadRunner extends App with Matchers {
  val m1: Monad[String, Int] = Happy(5)
  val m2: Monad[String, Int] = Happy(6)

  val um: Monad[String, Int] = Unhappy("error")

  def plus10(a: Int): Int = a + 10

  m1.map(_ + 10) shouldBe Happy(15)
  m1.flatMap(x ⇒ Happy(2 * x)) shouldBe Happy(10)
  um.map(_ + 10) shouldBe um
  um.map(x ⇒ Happy(2 * x)) shouldBe um

  val result = for {
    h1 ← m1
    h2 ← m2
  } yield h1 + h2

  val result2 = for {
    h1 ← m1
    h2 ← um
  } yield h1 + h2

  result shouldBe Happy(11)
  result2 shouldBe um

  val result3 = m1.flatMap(x ⇒ m2.map(y ⇒ y + x))
  result3 shouldBe Happy(11)


  println(result)
  println(m1.map(_ + 10))
  println(m1.flatMap(x ⇒ Happy(2 * x)))
  println(um.map(_ + 10))
  println(um.map(x ⇒ Happy(2 * x)))
}

object MonadRunner2 extends App with Matchers {

  import Monad2._

  val m1: Monad2[String, Int] = happy(5)
  val m2: Monad2[String, Int] = happy(6)

  val um: Monad2[String, Int] = unhappy("error")

  m1.map(_ + 10) shouldBe happy(15)
  m1.flatMap(x ⇒ happy(2 * x)) shouldBe happy(10)
  um.map(_ + 10) shouldBe um
  um.map(x ⇒ happy(2 * x)) shouldBe um

  val result = for {
    h1 ← m1
    h2 ← m2
  } yield h1 + h2

  val result2 = for {
    h1 ← m1
    h2 ← um
  } yield h1 + h2

  result shouldBe happy(11)
  result2 shouldBe um


  println(result)
  println(m1.map(_ + 10))
  println(m1.flatMap(x ⇒ happy(2 * x)))
  println(um.map(_ + 10))
  println(um.map(x ⇒ Happy(2 * x)))
}

