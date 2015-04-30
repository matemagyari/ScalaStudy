package org.home.bigdataaggregator

import org.home.bigdataaggregator.Monetary._

case class Input(createTransactionsIterator: Unit => Iterator[Transaction],
                 exchangeRates: ExchangeRates,
                 targetCurrency: Currency,
                 partner: Partner)


class AggregatorAppService(transactionAggregator: TransactionAggregator) {

  def aggregateTransactions(input: Input) = {
    val money = transactionAggregator.aggregateTransactionsOfPartner(
      input.createTransactionsIterator(),
      input.partner,
      input.targetCurrency,
      input.exchangeRates)

    val groupedTransactions = transactionAggregator.aggregateTransactionsByPartner(
      input.createTransactionsIterator(),
      input.targetCurrency,
      input.exchangeRates)

    (money, groupedTransactions)
  }
}

