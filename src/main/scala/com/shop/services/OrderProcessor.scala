package com.shop.services

import com.shop.models.{Product, AgeInterval, OrderGroup}
import com.shop.repository.OrderRepository

import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

class OrderProcessor(orderRepository: OrderRepository)(implicit ec: ExecutionContext) {

  def processOrders(
                     start: LocalDateTime,
                     end: LocalDateTime,
                     intervals: Seq[AgeInterval],
                   ): Future[Seq[OrderGroup]] = {
    for {
      filteredOrderIds <- orderRepository.filteredOrderIds(start, end)
      productOrderList <- orderRepository.getProductOrderList(filteredOrderIds)
    } yield calculateStatistics(productOrderList, intervals)
  }

  private def calculateStatistics(productOrderList: Seq[(Product, Long)], intervals: Seq[AgeInterval]): Seq[OrderGroup] = {
    val intervalCountsMap = scala.collection.mutable.Map[AgeInterval, Set[Long]]().withDefaultValue(Set.empty)

    productOrderList.foreach {
      case (product, orderId) =>
        val ageInMonths = calculateProductAgeInMonths(product)
        val assignedInterval = assignToInterval(ageInMonths, intervals)
        intervalCountsMap(assignedInterval) += orderId
    }

    val intervalCounts = intervals.map { interval =>
      OrderGroup(interval, intervalCountsMap(interval).size.toLong)
    }
    intervalCounts
  }

  private def calculateProductAgeInMonths(product: Product): Int = {
    val currentDate = LocalDateTime.now()
    val creationDate = product.creationDate.toLocalDateTime
    val months = java.time.temporal.ChronoUnit.MONTHS.between(creationDate, currentDate).toInt
    months
  }

  private def assignToInterval(ageInMonths: Int, intervals: Seq[AgeInterval]): AgeInterval = {
    val matchingInterval = intervals
      .find { case AgeInterval(start, end) => ageInMonths >= start && ageInMonths <= end }
      .getOrElse(AgeInterval(0, 0))
    matchingInterval
  }

}
