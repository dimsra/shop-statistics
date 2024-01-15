package com.shop.models

case class AgeInterval(
                        start: Int,
                        end: Int,
                      ) {
  override def toString: String = end match {
    case Int.MaxValue => s">${start - 1} months"
    case _ => s"$start-${end} months"
  }
}

case class OrderGroup(
                       interval: AgeInterval,
                       count: Long,
                     ) {

  def printResult(): Unit = {
    println(s"${interval.toString}: $count orders")
  }

}