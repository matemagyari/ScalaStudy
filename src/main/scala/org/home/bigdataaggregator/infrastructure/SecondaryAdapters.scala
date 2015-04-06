package org.home.bigdataaggregator.infrastructure

import org.home.bigdataaggregator.Monetary.Partner
import org.home.bigdataaggregator.Money

object SecondaryAdapters {

  object OutputPrinter {

    def printToConsole(money:Money) {
      Console.println(money)
    }
    def printToConsole(result:Map[Partner, Money]) {
      Console.println(result)
    }

    def printToFile(money:Money) {
      Console.println(money)
    }
  }

}
