package com.loganalyzer.controllers

import com.loganalyzer.models.LogModels._
import com.loganalyzer.services.LogAnalyzerService

class LogAnalyzerController(service: LogAnalyzerService) {
  def getStatus: String = """{"status":"ok"}"""

  def getLogSize: String = {
    val size = service.getLogFileSize
    s"""{"size":$size}"""
  }

  def analyzeData(request: LogRequest): LogResponse = {
    service.analyzeLogs(request)
  }

  def generateHistogram(request: LogRequest): HistogramResponse = {
    service.generateHistogram(request)
  }
}