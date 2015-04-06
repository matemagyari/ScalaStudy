package org.home.filemerger

import org.home.filemerger.Monetary.Partner
import org.home.filemerger.infrastructure.SecondaryAdapters.OutputPrinter
import org.home.filemerger.infrastructure.{ExchangeRatesFileReader, Orchestrator, TransactionsFileReader}

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