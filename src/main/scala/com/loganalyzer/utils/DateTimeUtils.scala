package com.loganalyzer.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeUtils {
  private val logDateFormatter = DateTimeFormatter.ofPattern("MMM d HH:mm:ss")

  def extractDateTime(line: String): LocalDateTime = {
    val timestamp = line.take(15)
    val currentYear = LocalDateTime.now().getYear
    LocalDateTime.parse(s"$currentYear $timestamp",
      DateTimeFormatter.ofPattern("yyyy MMM d HH:mm:ss"))
  }
  
}
