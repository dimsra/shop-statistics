package com.shop.repository

import com.shop.models.Product
import com.shop.tables.{Items, Orders, Products}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp
import java.time.LocalDateTime
import scala.concurrent.Future

class OrderRepository(db: Database) {

  val orders = TableQuery[Orders]
  val products = TableQuery[Products]
  val items = TableQuery[Items]

  def filteredOrderIds(start: LocalDateTime, end: LocalDateTime): Future[Seq[Long]] = db.run {
    orders
      .filter(order => order.orderDate >= Timestamp.valueOf(start) && order.orderDate <= Timestamp.valueOf(end))
      .map(_.id)
      .result
  }

  def getProductOrderList(filteredOrderIds: Seq[Long]): Future[Seq[(Product, Long)]] = db.run {
    items
      .join(products).on(_.productId === _.id)
      .filter(_._1.orderId inSet filteredOrderIds)
      .map { case (item, product) => (product, item.orderId) }
      .result
  }
}
