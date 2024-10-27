import java.nio.file.{Files, Paths}
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import scala.io.Source

object LogAnalyzer {

  import models.Models._

  private val logDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM d HH:mm:ss")

  def getLogFileSize: Long = {
    Files.size(Paths.get(Config.logFilePath))
  }

  // Extract datetime from the beginning of the log line
  private def extractDateTime(line: String): LocalDateTime = {
    val timestamp = line.take(15) // First 15 characters contain "Oct 26 08:14:22"
    val currentYear = LocalDateTime.now().getYear
    LocalDateTime.parse(s"$currentYear $timestamp", DateTimeFormatter.ofPattern("yyyy MMM d HH:mm:ss"))
  }


  private def extractMessage(line: String): String = {
    val parts = line.split("\\s+", 5) // Split line into 5 parts: date, time, hostname, service, message
    if (parts.length >= 5) parts(4) else ""
  }

  private def findHighlightPositions(message: String, phrase: String): List[HighlightText] = {
    val regex = phrase.r
    regex.findAllMatchIn(message).map(m => HighlightText(m.start, m.end)).toList
  }

  def analyzeLogs(request: LogRequest): LogResponse = {
    val logs = Source.fromFile(Config.logFilePath).getLines().toList
    val filteredLogs = logs.filter { line =>
      val datetime = extractDateTime(line)
      datetime.isAfter(request.datetimeFrom) &&
        datetime.isBefore(request.datetimeUntil) &&
        line.contains(request.phrase)
    }

    val data = filteredLogs.map { line =>
      val datetime = extractDateTime(line)
      val message = extractMessage(line)
      val highlights = findHighlightPositions(message, request.phrase)
      LogEntry(datetime.toString, message, highlights)
    }

    LogResponse(data, request.datetimeFrom.toString, request.datetimeUntil.toString, request.phrase)
  }

  //  def analyzeLogs(request: LogRequest): LogResponse = {
  //    val logs = Source.fromFile(Config.logFilePath).getLines().toList
  //    val filteredLogs = logs.filter { line =>
  //      val datetime = extractDateTime(line)
  //      datetime.isAfter(request.datetimeFrom) &&
  //        datetime.isBefore(request.datetimeUntil) &&
  //        line.contains(request.phrase)
  //    }
  //    val data = filteredLogs.map { line =>
  //      LogEntry(datetime = extractDateTime(line).toString, message = line)
  //    }
  //    LogResponse(data, request.datetimeFrom.toString, request.datetimeUntil.toString, request.phrase)
  //  }

  def generateHistogram(request: LogRequest): HistogramResponse = {
    val logs = Source.fromFile(Config.logFilePath).getLines().toList
    val filteredLogs = logs.filter { line =>
      val datetime = extractDateTime(line)
      datetime.isAfter(request.datetimeFrom) &&
        datetime.isBefore(request.datetimeUntil) &&
        line.contains(request.phrase)
    }

    val histogramData = filteredLogs.groupBy(line => extractDateTime(line).toLocalDate)
      .map { case (date, entries) => HistogramEntry(date.toString, entries.size) }
      .toList

    HistogramResponse(histogramData, request.datetimeFrom.toString, request.datetimeUntil.toString, request.phrase)
  }


}
