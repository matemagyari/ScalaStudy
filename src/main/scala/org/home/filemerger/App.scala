package org.home.filemerger

import org.home.filemerger.Monetary._

case class Input(transactionsFile: String, exchangeRatesFile: String, targetCurrency: Currency, partner: Partner)


class AggregatorAppService(readUpTransactions: String => Iterator[Transaction],
                           readUpExchangeRates: String => ExchangeRates,
                           transactionAggregator: TransactionAggregator) {

  def aggregateTransactions(input: Input) = {
    val exchangeRates = readUpExchangeRates(input.exchangeRatesFile)
    val transactionsIterator1 = readUpTransactions(input.transactionsFile)
    val money = transactionAggregator.aggregateTransactionsOfPartner(transactionsIterator1, input.partner, input.targetCurrency, exchangeRates)
    val transactionsIterator2 = readUpTransactions(input.transactionsFile)
    val groupedTransactions = transactionAggregator.aggregateTransactionsByPartner(transactionsIterator2, input.targetCurrency, exchangeRates)
    (money, groupedTransactions)
  }
}

