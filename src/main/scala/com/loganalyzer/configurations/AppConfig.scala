package com.loganalyzer.configurations

import com.typesafe.config.{Config, ConfigFactory}

object AppConfig {
  private val config = ConfigFactory.load()
  val host: String = config.getString("app.host")
  val port: Int = config.getInt("app.port")
  val logFilePath: String = config.getString("app.logFilePath")
}

