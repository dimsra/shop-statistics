package com.shop

import com.shop.models.{AgeInterval, OrderGroup}
import com.shop.repository.OrderRepository
import com.shop.services.OrderProcessor
import slick.jdbc.JdbcBackend.Database

import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

object MainApplication extends App {import com.typesafe.config.ConfigFactory

  lazy val db: Database = Database.forConfig("slick.dbs.default")
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  lazy val orderRepository = new OrderRepository(db)
  lazy val orderProcessor = new OrderProcessor(orderRepository)

  val (startDateTime, endDateTime, ageIntervals) = parseCommandLineArgs(args)

  val result: Future[Seq[OrderGroup]] = orderProcessor.processOrders(startDateTime, endDateTime, ageIntervals)
  result.onComplete {
    case scala.util.Success(intervalCounts) =>
      intervalCounts.foreach(_.printResult())
      db.close()

    case scala.util.Failure(exception) =>
      println(s"An error occurred: $exception")
      db.close()
  }
  //  scala.io.StdIn.readLine()


  private def parseCommandLineArgs(args: Array[String]): (LocalDateTime, LocalDateTime, Seq[AgeInterval]) = {
    if (args.length < 3) {
      println("Usage: MainApplication <startDateTime> <endDateTime> <interval1Start-interval1End> <interval2Start-interval2End> ...")
      System.exit(1)
    }

    val startDateTime = LocalDateTime.parse(args(0))
    val endDateTime = LocalDateTime.parse(args(1))
    val intervalArgs = args.drop(2).flatMap(parseInterval)
    (startDateTime, endDateTime, intervalArgs)
  }


  private def parseInterval(intervalArg: String): Option[AgeInterval] = {
    val parts = intervalArg.split("-")
    if (parts.length == 2) {
      Some(AgeInterval(parts(0).toInt, parts(1).toInt))
    } else if (parts.length == 1 && parts(0).startsWith(">")) {
      Some(AgeInterval(parts(0).substring(1).toInt + 1, Int.MaxValue))
    } else {
      println(s"Invalid interval argument: $intervalArg")
      None
    }
  }
}
