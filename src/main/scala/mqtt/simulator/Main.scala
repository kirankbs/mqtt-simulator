package mqtt.simulator

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import mqtt.simulator.api.API

import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem(Behaviors.empty, "mqtt-simulator")
  implicit val executionContext = system.executionContext

  // HTTP API to interact with Simulation
  val bindAndFuture = Http().newServerAt("localhost", 8080).bind(API.route)
  println(s"Server online at http://localhost:8080/ ...")
  StdIn.readLine()  //TODO Replace it
  bindAndFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())

}
