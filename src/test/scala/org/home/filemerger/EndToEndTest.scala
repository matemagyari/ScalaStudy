package org.home.filemerger

import org.home.filemerger.Monetary.Partner
import org.home.filemerger.TestFixture._
import org.home.filemerger.infrastructure.{Orchestrator, ExchangeRatesFileReader, TransactionsFileReader}

//import org.junit.Assert._
//import org.junit.Test

//class EndToEndTest {
object EndToEndTest extends App {

  val numOfTransactions = 10//1000 * 1000 * 10
  val transactionsFile = "transactions.csv"
  val exchangeRatesFile = "exchangeRates.csv"

  endToEndTest()

  //@Test
  def endToEndTest() {

    var result2: Money = null
    var result1: Map[Partner, Money] = null

    val orchestrator = new Orchestrator(PoormansDIContainer.getAppService(), m => { result1 = m }, m => { result2 = m })

    deleteFiles()

    createExchangeRatesFile(exchangeRatesFile)
    createTransactionsFile(transactionsFile, numOfTransactions)

    val input = Input(transactionsFile, exchangeRatesFile, currencies.head, partners.head)
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
