package org.home

import scala.collection.immutable.Seq
import scala.util.{Failure, Success, Try}

case class Vrm(value: String)

case class Car(vrm: Vrm, price: Long)

class CarNotFoundException(vrm: Vrm) extends RuntimeException

class CarStore(private val cars: Seq[Car]) {

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

object CarsRunner extends App {

  val vrm1 = Vrm("vrm1")
  val vrm2 = Vrm("vrm2")
  val cs = new CarStore(Seq(Car(vrm1, 20), Car(vrm2, 30)))

  println(cs.priceDifference(vrm1, vrm2))

}
