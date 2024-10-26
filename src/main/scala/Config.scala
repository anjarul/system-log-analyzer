import com.typesafe.config.{Config, ConfigFactory}

object Config {
  val config: Config = ConfigFactory.load()
  val logFilePath: String = config.getString("app.logFilePath")
}

