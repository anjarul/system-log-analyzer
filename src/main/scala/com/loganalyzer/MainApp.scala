package com.loganalyzer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.loganalyzer.configurations.AppConfig
import com.loganalyzer.routes.LogAnalyzerApiRoutes

import scala.concurrent.ExecutionContextExecutor

object MainApp extends App {
  implicit val system: ActorSystem = ActorSystem("LogAnalyzerSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val routes = new LogAnalyzerApiRoutes()
  Http().newServerAt(AppConfig.host, AppConfig.port).bind(routes.route)
  println(s"Server is running at http://${AppConfig.host}:${AppConfig.port}/")
}