package com.loganalyzer

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.loganalyzer.configurations.AppConfig
import com.loganalyzer.routes.LogAnalyzerApiRoutes

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}
import scala.concurrent.duration.*
import scala.io.StdIn

object MainApp extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "LogAnalyzerSystem")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext

  val routes = new LogAnalyzerApiRoutes()

  private val bindingFuture = Http().newServerAt(AppConfig.host, AppConfig.port)
    .bind(routes.route)

  bindingFuture.onComplete {
    case Success(binding) =>
      val address = binding.localAddress
      println(s"Server is running at http://${address.getHostString}:${address.getPort}/")
      println("Press RETURN to stop...")

    case Failure(ex) =>
      println(s"Failed to bind HTTP server: ${ex.getMessage}")
      system.terminate()
  }
  StdIn.readLine()

  sys.addShutdownHook {
    bindingFuture
      .flatMap(_.terminate(10.seconds))
      .onComplete(_ => system.terminate())
  }
}