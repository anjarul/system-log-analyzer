import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import models.JsonSupport._ // Import JSON support
import models.Models._

object Routes extends CORSHandler {
  // Helper method to parse the request asynchronously
  def parseRequest(entity: String): LogRequest = {
    import spray.json._
    entity.parseJson.convertTo[LogRequest]
  }

  val route: Route = corsHandler {
    concat(
      path("api" / "get_status") {
        get {
          complete(StatusCodes.OK -> """{"status":"ok"}""")
        }
      },
      path("api" / "get_size") {
        get {
          val size = LogAnalyzer.getLogFileSize
          complete(s"""{"size":$size}""")
        }
      },
      path("api" / "data") {
        post {
          entity(as[String]) { body =>
            val request = parseRequest(body) // Use parseRequest here
            val result = LogAnalyzer.analyzeLogs(request)
            complete(result)
          }
        }
      },
      path("api" / "histogram") {
        post {
          entity(as[String]) { body =>
            val request = parseRequest(body) // Use parseRequest here
            val histogram = LogAnalyzer.generateHistogram(request)
            complete(histogram)
          }
        }
      }
    )
  }
}
