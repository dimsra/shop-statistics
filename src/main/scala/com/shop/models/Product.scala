package com.shop.models

import java.sql.Timestamp

case class Product(
                    id: Long,
                    name: String,
                    category: String,
                    weight: Double,
                    price: Double,
                    creationDate: Timestamp,
                  )

