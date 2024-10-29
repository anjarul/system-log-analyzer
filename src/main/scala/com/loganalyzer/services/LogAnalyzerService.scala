package com.loganalyzer.services

import com.loganalyzer.configurations.AppConfig
import com.loganalyzer.models.LogModels.*
import com.loganalyzer.utils.DateTimeUtils

import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.util.Using

class LogAnalyzerService {
  def getLogFileSize: Long = Files.size(Paths.get(AppConfig.logFilePath))

  def analyzeLogs(request: LogRequest): LogResponse = {
    val filteredLogs = filterLogs(request)

    val data = filteredLogs.map { line =>
      val datetime = DateTimeUtils.extractDateTime(line)
      val message = extractMessage(line)
      val highlights = findHighlightPositions(message, request.phrase)
      LogEntry(datetime.toString, message, highlights)
    }

    LogResponse(data, request.datetimeFrom.toString,
      request.datetimeUntil.toString, request.phrase)
  }

  def generateHistogram(request: LogRequest): HistogramResponse = {
    val filteredLogs = filterLogs(request)

    val histogramData = filteredLogs.groupBy(line => DateTimeUtils.extractDateTime(line).toLocalDate)
      .map { case (date, entries) => HistogramEntry(date.toString, entries.size) }
      .toList

    HistogramResponse(histogramData, request.datetimeFrom.toString, request.datetimeUntil.toString, request.phrase)
  }

  /**
   * Finds all occurrences of a search phrase in a message, ignoring case sensitivity.
   * For example, searching for "error" will match "Error", "ERROR", "error", etc.
   */

  private def findHighlightPositions(message: String, phrase: String): List[HighlightText] = {
    val pattern = s"(?i)$phrase".r
    pattern.findAllMatchIn(message).map(m => HighlightText(m.start, m.end)).toList
  }

  private def extractMessage(line: String): String = {
    val parts = line.split("\\s+", 5)
    if (parts.length >= 5) parts(4) else ""
  }

  private def filterLogs(request: LogRequest): List[String] = {
    Using(Source.fromFile(AppConfig.logFilePath)) { source =>
      source.getLines().toList.filter { line =>
        val datetime = DateTimeUtils.extractDateTime(line)
        datetime.isAfter(request.datetimeFrom) &&
          datetime.isBefore(request.datetimeUntil) &&
          line.contains(request.phrase)
      }
    }.getOrElse(Nil)
  }
}