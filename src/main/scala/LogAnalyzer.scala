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

  def analyzeLogs(request: LogRequest): LogResponse = {
    val logs = Source.fromFile(Config.logFilePath).getLines().toList
    val filteredLogs = logs.filter { line =>
      val datetime = extractDateTime(line)
      datetime.isAfter(request.datetimeFrom) &&
        datetime.isBefore(request.datetimeUntil) &&
        line.contains(request.phrase)
    }
    val data = filteredLogs.map { line =>
      LogEntry(datetime = extractDateTime(line).toString, message = line)
    }
    LogResponse(data, request.datetimeFrom.toString, request.datetimeUntil.toString, request.phrase)
  }

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

  private def extractDateTime(line: String): LocalDateTime = {
    // Parse datetime from log line (adjust parsing as per log format)
//    LocalDateTime.parse(line.take(16)) // Assuming the first 19 characters are datetime
    val timestamp = line.take(15) // Extract the first 15 characters (e.g., "Oct 26 08:14:22")
    val currentYear = LocalDateTime.now().getYear // Assume the current year
    LocalDateTime.parse(s"$currentYear $timestamp", DateTimeFormatter.ofPattern("yyyy MMM d HH:mm:ss"))

  }
}
