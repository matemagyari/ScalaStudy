package org.home.bigdataaggregator.infrastructure

import org.home.bigdataaggregator.{AggregatorAppService, Money, Input}
import org.home.bigdataaggregator.Monetary._

class Orchestrator(aggregatorAppService: AggregatorAppService,
                   toOutput1: Map[Partner, Money] => Unit,
                   toOutput2: Money => Unit) {

  def aggregate(input:Input) {
    val aggregates = aggregatorAppService.aggregateTransactions(input)
    toOutput1(aggregates._2)
    toOutput2(aggregates._1)
  }

}
