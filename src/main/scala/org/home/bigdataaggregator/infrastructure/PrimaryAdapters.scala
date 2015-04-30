package org.home.bigdataaggregator.infrastructure

import org.home.bigdataaggregator.Monetary._
import org.home.bigdataaggregator.{Input, Transaction, Money}

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

  type ExchangeRate = ((Currency, Currency), BigDecimal)

  def lineToExchangeRate(line: String) = {
    val splitLine = CSVUtil.splitCSLine(line)
    ((splitLine(0), splitLine(1)), BigDecimal(splitLine(2)))
  }

  def addToRates(rates: ExchangeRates, rate: ExchangeRate) = rates + (rate._1 -> rate._2)

  def fileUploader(file: String) = Source.fromFile(file).getLines()
                                      .map(lineToExchangeRate)
                                      .foldLeft(Map.empty[(Currency, Currency), BigDecimal])(addToRates)

}

case class CLInput(transactionsFile: String, exchangeRatesFile: String, targetCurrency: Currency, partner: Partner)

object InputAssembler {
  def assemble(clInput: CLInput) = {
    val exchangeRates = ExchangeRatesFileReader.fileUploader(clInput.exchangeRatesFile)
    val createTransactionsIterator:Unit => Iterator[Transaction] = Unit => TransactionsFileReader.getTransactionsStream(clInput.transactionsFile)
    Input(createTransactionsIterator, exchangeRates, clInput.targetCurrency, clInput.partner)
  }
}

