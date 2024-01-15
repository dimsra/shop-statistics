package com.shop.models

case class Item(
                 id: Long,
                 orderId: Long,
                 productId: Long,
                 cost: Double,
                 shippingFee: Double,
                 taxAmount: Double,
               )