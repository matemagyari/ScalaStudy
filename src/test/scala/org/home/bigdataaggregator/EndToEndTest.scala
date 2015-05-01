package org.home.bigdataaggregator

import org.home.bigdataaggregator.Monetary.Partner
import org.home.bigdataaggregator.TestFixture._
import org.home.bigdataaggregator.infrastructure._
import org.junit.Test

class EndToEndTest {

  val numOfTransactions = 1000 * 1000 * 100
  val transactionsFile = "/Users/mate.magyari/Downloads/transactions/transactions.csv"
  val exchangeRatesFile = "/Users/mate.magyari/Downloads/exchangeRates.csv"

  @Test
  def endToEndTest() {

    var result2: Money = null
    var result1: Map[Partner, Money] = null

    val orchestrator = new Orchestrator(PoormansDIContainer.getAppService(),
      InputAssembler.assemble,
      m => {
        result1 = m
      },
      m => {
        result2 = m
      })

    //    deleteFiles()
    //
    //    val start0 = System.currentTimeMillis()
    //
    //    createExchangeRatesFile(exchangeRatesFile)
    //    createTransactionsFile(transactionsFile, numOfTransactions)
    //
    //    Console.println("Generation time: " + (System.currentTimeMillis() - start0))

    val input = CLInput(transactionsFile, exchangeRatesFile, "GBP", "MRN8w")
    val start = System.currentTimeMillis()
    orchestrator.aggregate(input)
    //assertNotNull(result1)
    //assertNotNull(result2)

    Console.println("Result1: " + result1)
    Console.println("Result2: " + result2)
    Console.println("Time: " + (System.currentTimeMillis() - start))
    //deleteFiles()
  }

  def deleteFiles() {
    delete(transactionsFile)
    delete(exchangeRatesFile)
  }


}
