package mqtt.simulator

import java.time.ZonedDateTime
import java.util.UUID

import akka.Done
import akka.actor.CoordinatedShutdown
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import mqtt.simulator.Main.{bindAndFuture, system}
import mqtt.simulator.api.API
import mqtt.simulator.storage.{FlywayMigration, SimulationDefinitionRepo}
import slick.jdbc.JdbcBackend.Database
import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

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
  val host = "0.0.0.0"
  val port = 9000
  val bindAndFuture =
    Http()
      .newServerAt(host, port)
      .bind(api.routes)

  println(s"Server online at http://$host:$port/ ...")
  ServerShutdown()
}

object ServerShutdown {
  def apply()(implicit ec: ExecutionContext, system: ActorSystem[Nothing]) = {
    val shutdown = CoordinatedShutdown(system)
    shutdown.addTask(CoordinatedShutdown.PhaseServiceUnbind, "http-unbind") { () =>
      bindAndFuture.flatMap(_.unbind()).map(_ => Done)
    }
    shutdown.addTask(CoordinatedShutdown.PhaseServiceRequestsDone, "http-graceful-terminate") { () =>
      bindAndFuture.flatMap(_.terminate(10.seconds)).map(_ => Done)
    }
    shutdown.addTask(CoordinatedShutdown.PhaseServiceStop, "http-shutdown") { () =>
      Http().shutdownAllConnectionPools().map(_ => Done)
    }
  }
}
