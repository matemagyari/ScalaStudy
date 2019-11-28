name := "ScalaStudy"

organization := "org.home"

version := "1.0"

scalaVersion := "2.12.1"

libraryDependencies ++= {
  val slf4jVersion = "1.7.21"
  val logbackVersion = "1.1.3"
  Seq(
    //Logging
    "org.slf4j" % "slf4j-api" % slf4jVersion,
    "org.slf4j" % "log4j-over-slf4j" % slf4jVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion % "runtime",
    "ch.qos.logback" % "logback-core" % logbackVersion % "runtime",
    "org.typelevel" %% "cats" % "0.9.0",
    "com.typesafe" % "config" % "1.3.0",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    "org.scalatest" %% "scalatest" % "3.0.1")
}
    