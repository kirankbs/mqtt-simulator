package mqtt.simulator.api

import java.time.ZonedDateTime
import java.util.UUID

import akka.Done
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Directives.{complete, path}
import mqtt.simulator.api.models.{SimulationDefinition, SimulationDefinitionRequest}
import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object API {

  var simulations = ListBuffer.empty[SimulationDefinition]

  val route = {
    pathPrefix("simulation") {
        get {
          pathEnd {
            complete(simulations.toList)
          }
      } ~ get {
        path(JavaUUID) { id =>
          onSuccess(fetchSimulation(id)) {
            case Some(simulation) => complete(simulation)
            case None => complete(StatusCodes.NoContent)
          }
        }
      } ~ post {
          pathEnd {
            entity(as[SimulationDefinitionRequest]) { definitionReq =>
              //TODO 201 and some response
              onSuccess(createSimulationDefinition(definitionReq)) { _ => complete(StatusCodes.Created) }
            }
          }
      } ~ patch {
          path(JavaUUID) { id =>
            entity(as[SimulationDefinitionRequest]) { sdfr =>
              onSuccess(patchSimulationDefinition(id, sdfr)) { _ => complete(StatusCodes.Created)}
            }
          }
        }
    }
  }

  def createSimulationDefinition(sdfr: SimulationDefinitionRequest): Future[Done] = {
    val sdf = SimulationDefinition(
      UUID.randomUUID(),
      sdfr.message,
      ZonedDateTime.now(),
      sdfr.startAt,
      sdfr.startAt,
      sdfr.endAt
    )
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

  def patchSimulationDefinition(id: UUID, sdfr: SimulationDefinitionRequest): Future[Done] = {
    val sdfNew =
      simulations
      .find(_.id == id)
      .map(_.copy(message = sdfr.message, startAt = sdfr.startAt, endAt = sdfr.endAt))

    println("patch "+ sdfNew)

    simulations = sdfNew.fold(simulations)(newValue => simulations.map{ sdf => if(sdf.id == id) newValue else sdf})

    Future {
      Done
    }
  }

}
