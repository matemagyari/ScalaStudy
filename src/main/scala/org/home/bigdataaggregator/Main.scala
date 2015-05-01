package org.home.bigdataaggregator

import org.home.bigdataaggregator.Monetary.Partner
import org.home.bigdataaggregator.infrastructure.SecondaryAdapters.OutputPrinter
import org.home.bigdataaggregator.infrastructure._

object Main extends App {

  val orchestrator = PoormansDIContainer.getOrcherstrator()

  val input = CLInput("/Users/mate.magyari/Downloads/transactions/transactions.csv", "", "GBP", "")

  orchestrator.aggregate(input)
}

object PoormansDIContainer {

  def getOrcherstrator() = new Orchestrator(
    getAppService(),
    InputAssembler.assemble,
    OutputPrinter.printToConsole(_: Map[Partner, Money]),
    OutputPrinter.printToConsole(_: Money))

  def getAppService() = new AggregatorAppService(new TransactionAggregator())


}