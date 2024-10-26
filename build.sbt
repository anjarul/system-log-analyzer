ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.3.4"

lazy val root = (project in file("."))
  .settings(
    name := "systemLogAnalyzer"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.8.6",
  "com.typesafe.akka" %% "akka-http" % "10.5.3",
  "com.typesafe.akka" %% "akka-stream" % "2.8.6",
  "ch.qos.logback" % "logback-classic" % "1.5.6",
  "io.spray" %% "spray-json" % "1.3.6",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.5.3"
)
