package models

object Models {

  import java.time.LocalDateTime

  case class LogRequest(datetimeFrom: LocalDateTime, datetimeUntil: LocalDateTime, phrase: String)

  case class LogEntry(datetime: String, message: String)

  case class LogResponse(data: List[LogEntry], datetimeFrom: String, datetimeUntil: String, phrase: String)

  case class HistogramEntry(datetime: String, counts: Int)

  case class HistogramResponse(histogram: List[HistogramEntry], datetimeFrom: String, datetimeUntil: String, phrase: String)
}