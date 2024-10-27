package com.loganalyzer

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.loganalyzer.configurations.AppConfig
import com.loganalyzer.routes.ApiRoutes

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object MainApp extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "LogAnalyzerSystem")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val routes = new ApiRoutes()

  val bindingFuture = Http().newServerAt(AppConfig.host, AppConfig.port)
    .bind(routes.route)

  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"Server is running at http://${address.getHostString}:${address.getPort}/")

    case Failure(ex) =>
      println(s"Failed to bind HTTP server: ${ex.getMessage}")
      system.terminate()
  }

  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.terminate(10.seconds))
      .onComplete(_ => system.terminate())
  }
}