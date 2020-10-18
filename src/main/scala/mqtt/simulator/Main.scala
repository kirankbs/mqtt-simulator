package mqtt.simulator

import java.time.ZonedDateTime
import java.util.UUID

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import mqtt.simulator.api.API
import mqtt.simulator.storage.{FlywayMigration, SimulationDefinitionRepo}
import slick.jdbc.JdbcBackend.Database

import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem(Behaviors.empty, "mqtt-simulator")
  implicit val executionContext = system.executionContext

  //Config
  val config = ConfigFactory.load()

  //FlyWay
  val db = Database.forConfig("db")
  FlywayMigration(config, "db")

  // HTTP API to interact with Simulation
  val api = new API(new SimulationDefinitionRepo(db), UUID.randomUUID, ZonedDateTime.now)
  val bindAndFuture = Http().newServerAt("localhost", 8080).bind(api.routes)
  println(s"Server online at http://localhost:8080/ ...")
  StdIn.readLine() //TODO Replace it
  bindAndFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
