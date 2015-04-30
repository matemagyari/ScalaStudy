package org.home.bigdataaggregator

import java.io.{File, FileWriter}

import scala.math.BigDecimal.RoundingMode
import scala.util.Random

object TestFixture {

  val currencies = {
    val letters = 'A' to 'Z'
    val lazyList = for {c1 <- letters
                        c2 <- letters
                        c3 <- letters} yield s"$c1$c2$c3"
    lazyList.take(100).toList
  }

  val partners = {
    val lazyList = for {i <- 1 to 100} yield "P" + i
    lazyList.take(100).toList
  }

  def createExchangeRatesFile(file: String) {

    val exchangeRates = for {
      sourceCurrency <- currencies
      targetCurrency <- currencies if targetCurrency != sourceCurrency
      rate = randomNumber()
    } yield (sourceCurrency, targetCurrency, rate)

    val fw = new FileWriter(file, true)
    try {
      exchangeRates.foreach(rate => fw.append(rate._1 + "," + rate._2 + "," + rate._3 + "\n"))
    } finally fw.close()
  }

  def createTransactionsFile(file: String, numOfTransactions: Int) {
    val fw = new FileWriter(file, true)
    try {
      (1 to numOfTransactions).foreach { i =>

        val partner = Random.shuffle(partners).head
        val currency = Random.shuffle(currencies).head
        val amount = randomNumber()
        fw.append(partner + "," + currency + "," + amount + "\n")
      }
    } finally fw.close()

  }

  def randomNumber() = {
    val n = BigDecimal(Random.nextDouble())
    val scale = Math.min(3, n.scale)
    n.setScale(scale, RoundingMode.HALF_DOWN)
  }

  def delete(file: String) = new File(file).delete()

}
