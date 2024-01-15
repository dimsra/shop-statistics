import com.shop.models._
import org.scalatest.flatspec.AsyncFlatSpec
import org.scalatest.matchers.should.Matchers
import org.mockito.MockitoSugar._
import com.shop.repository.OrderRepository
import com.shop.services.OrderProcessor

import scala.concurrent.Future
import java.sql.Timestamp
import java.time.{LocalDateTime, ZoneOffset}

class OrderProcessorSpec extends AsyncFlatSpec with Matchers {

  val mockOrderRepository: OrderRepository = mock[OrderRepository]
  val orderProcessor = new OrderProcessor(mockOrderRepository)

  "OrderProcessor" should "generate correct com.shop.statistics for product age intervals" in {
    val startDateTime = LocalDateTime.of(2023, 1, 1, 0, 0)
    val endDateTime = LocalDateTime.of(2023, 12, 31, 23, 59)
    val now = LocalDateTime.of(2024, 1, 1, 0, 0)
    val ageIntervals = Seq(
      AgeInterval(0, 3),
      AgeInterval(4, 5),
      AgeInterval(6, 8),
      AgeInterval(9, Int.MaxValue)
    )

    val orders = Seq(
      Order(1, "Customer1", "Address1", 100.0, Timestamp.valueOf(now.minusMonths(1))),
      Order(2, "Customer2", "Address2", 150.0, Timestamp.valueOf(now.minusMonths(2))),
      Order(3, "Customer3", "Address3", 120.0, Timestamp.valueOf(now.minusMonths(5)))
    )

    val products = Seq(
      Product(1, "Product1", "Category1", 1.0, 10.0, Timestamp.valueOf(now.minusMonths(2))),
      Product(2, "Product2", "Category2", 2.0, 15.0, Timestamp.valueOf(now.minusMonths(5))),
      Product(3, "Product3", "Category3", 3.0, 20.0, Timestamp.valueOf(now.minusMonths(8))),
      Product(4, "Product4", "Category4", 4.0, 25.0, Timestamp.valueOf(now.minusMonths(10)))
    )

    val items = Seq(
      Item(1, 1, 1, 10.0, 2.0, 1.0),
      Item(2, 1, 2, 15.0, 3.0, 1.5),
      Item(3, 2, 1, 25.0, 4.0, 2.0),
      Item(4, 2, 3, 20.0, 4.0, 2.0),
      Item(5, 3, 4, 25.0, 5.0, 2.5)
    )

    when(mockOrderRepository.filteredOrderIds(startDateTime, endDateTime))
      .thenReturn(Future.successful(orders.map(_.id)))

    when(mockOrderRepository.getProductOrderList(orders.map(_.id)))
      .thenReturn(Future.successful(items.map(item => (products.find(_.id == item.productId).get, item.orderId))))

    val result: Future[Seq[OrderGroup]] = orderProcessor.processOrders(startDateTime, endDateTime, ageIntervals)

    result.map { intervalCounts =>
      intervalCounts should have length 4
      intervalCounts.find(_.interval == AgeInterval(0, 3)).get.count shouldEqual 2
      intervalCounts.find(_.interval == AgeInterval(4, 5)).get.count shouldEqual 1
      intervalCounts.find(_.interval == AgeInterval(6, 8)).get.count shouldEqual 1
      intervalCounts.find(_.interval == AgeInterval(9, Int.MaxValue)).get.count shouldEqual 1
    }
  }
}
