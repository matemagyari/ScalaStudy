package org.home.filemerger.infrastructure

import org.home.filemerger.Monetary.{Currency, ExchangeRates}
import org.home.filemerger.{Transaction, Money}

import scala.io.Source

object CSVUtil {
  def splitCSLine(line: String) = line.split(",").map(_.trim)
}

object TransactionsFileReader {

  def lineToTransaction(line: String) = {
    val splitLine = CSVUtil.splitCSLine(line)
    val partner = splitLine(0)
    val currency = splitLine(1)
    val amount = BigDecimal(splitLine(2))
    Transaction(partner, Money(amount, currency))
  }

  def getTransactionsStream(file: String) = Source.fromFile(file).getLines().map(lineToTransaction)
}

object ExchangeRatesFileReader {

  type ExchangeRate = (Currency, Currency, BigDecimal)

  def lineToExchangeRate(line: String) = {
    val splitLine = CSVUtil.splitCSLine(line)
    (splitLine(0), splitLine(1), BigDecimal(splitLine(2)))
  }

  def addToRates(rates: ExchangeRates, rate: ExchangeRate): ExchangeRates = rates + ((rate._1, rate._2) -> rate._3)

  def fileUploader(file: String) = Source.fromFile(file).getLines()
                                      .map(lineToExchangeRate)
                                      .foldLeft(Map.empty[(Currency, Currency), BigDecimal])(addToRates)

}
