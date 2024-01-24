name := "shop-statistics"

version := "1.0"

scalaVersion := "2.13.8"

libraryDependencies ++= Seq(
  "com.typesafe.slick" %% "slick" % "3.3.3",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.3",
  "org.postgresql" % "postgresql" % "42.5.4",
  "org.scalatest" %% "scalatest" % "3.2.10" % "test",
  "org.mockito" %% "mockito-scala-scalatest" % "1.16.46" % "test",
)

scalacOptions ++= Seq(
  "-unchecked",
  "-deprecation",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions"
)

lazy val root = (project in file("."))
  .settings(
    name := "shop-statistics"
  )
