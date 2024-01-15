package com.shop.models

import java.sql.Timestamp

case class Order(
                  id: Long,
                  customerName: String,
                  shippingAddress: String,
                  grandTotal: Double,
                  orderDate: Timestamp,
                )

