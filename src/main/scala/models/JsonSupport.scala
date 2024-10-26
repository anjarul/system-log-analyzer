package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport // Import SprayJsonSupport
import spray.json._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import models.Models._

object JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  // Custom JSON format for LocalDateTime
  implicit object LocalDateTimeFormat extends RootJsonFormat[LocalDateTime] {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")

    def write(dateTime: LocalDateTime): JsValue = JsString(dateTime.format(formatter))

    def read(value: JsValue): LocalDateTime = value match {
      case JsString(str) => LocalDateTime.parse(str, formatter)
      case _ => throw new DeserializationException("Invalid datetime format")
    }
  }

  // Define implicit formats for request and response models
  implicit val logRequestFormat: RootJsonFormat[LogRequest] = jsonFormat3(LogRequest)
  implicit val logEntryFormat: RootJsonFormat[LogEntry] = jsonFormat2(LogEntry)
  implicit val logResponseFormat: RootJsonFormat[LogResponse] = jsonFormat4(LogResponse)
  implicit val histogramEntryFormat: RootJsonFormat[HistogramEntry] = jsonFormat2(HistogramEntry)
  implicit val histogramResponseFormat: RootJsonFormat[HistogramResponse] = jsonFormat4(HistogramResponse)
}