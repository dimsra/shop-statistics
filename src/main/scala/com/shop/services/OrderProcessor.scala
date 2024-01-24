package com.shop.services

import com.shop.models.{AgeInterval, OrderGroup, Product}
import com.shop.repository.OrderRepository

import java.time.{LocalDateTime, Period}
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
    val intervalCountsMap = productOrderList
      .groupBy { case (product, _) =>
        intervals.find(interval => {
          val ageInMonths = calculateProductAgeInMonths(product)
          ageInMonths >= interval.start && ageInMonths <= interval.end
        })
      }
      .view
      .mapValues(_.map { case (_, orderId) => orderId }.toSet)

    intervals.map { interval =>
      OrderGroup(interval, intervalCountsMap.getOrElse(Some(interval), Set.empty).size.toLong)
    }
  }

  private def calculateProductAgeInMonths(product: Product): Int = {
    val currentDate = LocalDateTime.now()
    val creationDate = product.creationDate.toLocalDateTime
    Period.between(creationDate.toLocalDate, currentDate.toLocalDate).toTotalMonths.toInt
  }

}
