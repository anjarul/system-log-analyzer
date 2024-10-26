import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import scala.concurrent.ExecutionContextExecutor
import Config._

object MainApp extends App {
  implicit val system: ActorSystem = ActorSystem("LogAnalyzerSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val host = config.getString("app.host")
  val port = config.getInt("app.port")

  Http().newServerAt(host, port).bind(Routes.route)
  println(s"Server is running at http://$host:$port/")
}
