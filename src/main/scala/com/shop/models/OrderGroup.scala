package com.shop.models

case class AgeInterval(
                        start: Int,
                        end: Int,
                      )

case class OrderGroup(
                       interval: AgeInterval,
                       count: Long,
                     ) {

  def printResult(): Unit = {
    println(s"${interval.start}-${interval.end} months: ${count} orders")
  }
}