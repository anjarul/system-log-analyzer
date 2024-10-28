package com.loganalyzer.routes

import akka.http.scaladsl.server.Directives.*
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import com.loganalyzer.controllers.LogAnalyzerController
import com.loganalyzer.services.LogAnalyzerService
import com.loganalyzer.utils.CORSHandler
import com.loganalyzer.models.JsonFormats.*
import com.loganalyzer.models.LogModels.LogRequest

class ApiRoutes extends CORSHandler {
  private val service = new LogAnalyzerService()
  private val controller = new LogAnalyzerController(service)

  val route: Route = corsHandler {
    pathPrefix("api") {
      concat(
        path("get_status") {
          get {
            complete(StatusCodes.OK -> controller.getStatus)
          }
        },
        path("get_size") {
          get {
            complete(controller.getLogSize)
          }
        },
        path("data") {
          post {
            entity(as[LogRequest]) { request =>
              complete(controller.analyzeData(request))
            }
          }
        },
        path("histogram") {
          post {
            entity(as[LogRequest]) { request =>
              complete(controller.generateHistogram(request))
            }
          }
        }
      )
    }
  }
}