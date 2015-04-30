package org.home.bigdataaggregator.infrastructure

import org.home.bigdataaggregator.{Input, AggregatorAppService, Money}
import org.home.bigdataaggregator.Monetary._

class Orchestrator(aggregatorAppService: AggregatorAppService,
                   inputAssembler: CLInput => Input,
                   toOutput1: Map[Partner, Money] => Unit,
                   toOutput2: Money => Unit) {

  def aggregate(clInput:CLInput) {
    val input = inputAssembler(clInput)
    val aggregates = aggregatorAppService.aggregateTransactions(input)
    toOutput1(aggregates._2)
    toOutput2(aggregates._1)
  }

}
