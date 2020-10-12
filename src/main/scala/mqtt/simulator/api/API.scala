package mqtt.simulator.api

import java.time.ZonedDateTime
import java.util.UUID

import akka.Done
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, path}
import mqtt.simulator.api.models.SimulationDefinition

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._

object API {

  var simulations = ListBuffer(
    SimulationDefinition(
      UUID.fromString("5fe78d07-d3be-4929-baa9-ab434264db64"),
      ZonedDateTime.now(), None, None, None
    )
  )

  val route = {
    pathPrefix("simulation") {
        get {
          pathEnd {
            complete(HttpEntity(ContentTypes.`application/json`, "Found all"))
          }
      } ~ get {
        path(JavaUUID) { id =>
          onSuccess(fetchSimulation(id)) {
            case Some(simulation) => complete(simulation)
            case None => complete(StatusCodes.NoContent)
          }
        }
      } ~ post {
          entity(as[SimulationDefinition]){ definition =>
            complete(HttpEntity(ContentTypes.`application/json`, "Created"))
          }
      }
    }
  }

  def saveSimulationDefinitions(sdf: SimulationDefinition): Future[Done] = {
    simulations.addOne(sdf)
    Future {
      Done
    }
  }

  def fetchSimulationDefinitions(): Future[List[SimulationDefinition]] =
    Future {
      simulations.toList
    }

  def fetchSimulation(simulationId: UUID): Future[Option[SimulationDefinition]] =
    Future {
      simulations.find(_.id == simulationId)
    }

}
