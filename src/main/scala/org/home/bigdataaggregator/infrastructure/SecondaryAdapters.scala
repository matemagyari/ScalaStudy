package org.home.bigdataaggregator.infrastructure

import java.io.FileWriter

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
    def printToFile(result:Map[Partner, Money]) {
      val fileName = "aggregated_transactions_by_partner.csv"
      val fw = new FileWriter(fileName, false)
      try {
        result.foreach(kv => fw.append(kv._1 + "," + kv._2 +"\n"))
      } finally fw.close()
    }
  }

}
