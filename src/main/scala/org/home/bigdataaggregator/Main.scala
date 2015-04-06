package org.home.bigdataaggregator

import org.home.bigdataaggregator.Monetary.Partner
import org.home.bigdataaggregator.infrastructure.SecondaryAdapters.OutputPrinter
import org.home.bigdataaggregator.infrastructure.{ExchangeRatesFileReader, Orchestrator, TransactionsFileReader}

object Main extends App {

  val orchestrator = PoormansDIContainer.getOrcherstrator()

  val input = Input("", "", "USD", "")

  orchestrator.aggregate(input)
}

object PoormansDIContainer {

  def getOrcherstrator() = new Orchestrator(
    getAppService(),
    OutputPrinter.printToConsole(_: Map[Partner, Money]),
    OutputPrinter.printToConsole(_: Money))

  def getAppService() = new AggregatorAppService(
    TransactionsFileReader.getTransactionsStream,
    ExchangeRatesFileReader.fileUploader,
    new TransactionAggregator())


}