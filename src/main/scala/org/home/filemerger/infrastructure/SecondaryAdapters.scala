package org.home.filemerger.infrastructure

import org.home.filemerger.Monetary.Partner
import org.home.filemerger.Money

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
