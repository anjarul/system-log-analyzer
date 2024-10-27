package com.loganalyzer.routes

import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.StatusCodes
import com.loganalyzer.controllers.LogAnalyzerController
import com.loganalyzer.services.LogAnalyzerService
import com.loganalyzer.utils.CORSHandler
import com.loganalyzer.models.JsonFormats.*
import com.loganalyzer.models.LogModels.LogRequest

class ApiRoutes extends CORSHandler {
  private val service = new LogAnalyzerService()
  private val controller = new LogAnalyzerController(service)

  val route = corsHandler {
    concat(
      path("api" / "get_status") {
        get {
          complete(StatusCodes.OK -> controller.getStatus)
        }
      },
      path("api" / "get_size") {
        get {
          complete(controller.getLogSize)
        }
      },
      path("api" / "data") {
        post {
          entity(as[LogRequest]) { request =>
            complete(controller.analyzeData(request))
          }
        }
      },
      path("api" / "histogram") {
        post {
          entity(as[LogRequest]) { request =>
            complete(controller.generateHistogram(request))
          }
        }
      }
    )
  }
}