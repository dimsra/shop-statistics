package com.shop.tables
import com.shop.models.{Item, Order, Product}
import slick.jdbc.PostgresProfile.api._

import java.sql.Timestamp

class Orders(tag: Tag) extends Table[Order](tag, "orders") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def customerName = column[String]("customer_name")
  def shippingAddress = column[String]("shipping_address")
  def grandTotal = column[Double]("grand_total")
  def orderDate = column[Timestamp]("order_date")
  def * = (id, customerName, shippingAddress, grandTotal, orderDate).mapTo[Order]
}

class Items(tag: Tag) extends Table[Item](tag, "items") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def orderId = column[Long]("order_id")
  def productId = column[Long]("product_id")
  def cost = column[Double]("cost")
  def shippingFee = column[Double]("shipping_fee")
  def taxAmount = column[Double]("tax_amount")
  def * = (id, orderId, productId, cost, shippingFee, taxAmount).mapTo[Item]
}

class Products(tag: Tag) extends Table[Product](tag, "products") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def category = column[String]("category")
  def weight = column[Double]("weight")
  def price = column[Double]("price")
  def creationDate = column[Timestamp]("creation_date")
  def * = (id, name, category, weight, price, creationDate).mapTo[Product]
}