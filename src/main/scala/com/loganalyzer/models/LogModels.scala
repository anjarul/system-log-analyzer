package com.loganalyzer.models

import java.time.LocalDateTime

object LogModels {
  case class HighlightText(fromPosition: Int, toPosition: Int)

  case class LogRequest(datetimeFrom: LocalDateTime, datetimeUntil: LocalDateTime, phrase: String)

  case class LogEntry(datetime: String, message: String, highlightText: List[HighlightText])

  case class LogResponse(
                          data: List[LogEntry],
                          datetimeFrom: String,
                          datetimeUntil: String,
                          phrase: String
                        )

  case class HistogramEntry(datetime: String, counts: Int)

  case class HistogramResponse(
                                histogram: List[HistogramEntry],
                                datetimeFrom: String,
                                datetimeUntil: String,
                                phrase: String
                              )
}