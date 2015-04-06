package org.home.bigdataaggregator

import org.home.bigdataaggregator.Monetary.{Partner, ExchangeRates, Currency}

object Monetary {

  type Currency = String
  type ExchangeRates = Map[(Currency, Currency), BigDecimal]
  type Partner = String

}

case class Transaction(partner:Partner, money:Money)

case class Money(amount: BigDecimal = BigDecimal(0), currency: Currency) {

  def +(addition: Money) = copy(amount + addition.amount, currency)

  def convert(targetCurrency: Currency, exchangeRates: ExchangeRates) = targetCurrency match {
    case currency => this
    case _ => copy(amount * exchangeRates(currency, targetCurrency), targetCurrency)
  }
}

class TransactionAggregator {

  def aggregateTransactionsOfPartner(transactions:Iterator[Transaction],
                                     partner:Partner,
                                     targetCurrency:Currency,
                                     exchangeRates: ExchangeRates) =

    transactions.filter( t => t.partner.equals(partner))
                .map( t => t.money.convert(targetCurrency,exchangeRates) )
                .foldLeft(Money(currency = targetCurrency))(_+_)

  def aggregateTransactionsByPartner(transactions:Iterator[Transaction],
                                     targetCurrency:Currency,
                                     exchangeRates: ExchangeRates) = {

    def accumulate(acc:Map[Partner, Money], transaction:Transaction) = {
      val currentValue = acc.getOrElse(transaction.partner, Money(currency = targetCurrency))
      val updatedValue = currentValue + transaction.money.convert(targetCurrency, exchangeRates)
      acc + (transaction.partner -> updatedValue)
    }

    transactions.foldLeft(Map.empty[Partner, Money])(accumulate)
  }

}



