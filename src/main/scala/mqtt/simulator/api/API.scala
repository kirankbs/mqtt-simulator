package mqtt.simulator.api

import java.time.ZonedDateTime
import java.util.UUID
import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{complete, path, _}
import mqtt.simulator.api.models.{SimulationDefinition, SimulationDefinitionRequest}
import mqtt.simulator.storage.SimulationDefinitionRepo
import spray.json.DefaultJsonProtocol._
import scala.concurrent.Future

class API(simulationDefRepo: SimulationDefinitionRepo) {

  val route = {
    pathPrefix("simulation") {
        get {
          pathEnd {
            complete(getSimulationDefinitions())
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
              onSuccess(patchSimulationDefinition(id, sdfr)) { _ => complete(StatusCodes.NoContent)}
            }
          }
        } ~ delete {
          path(JavaUUID) { id =>
            onSuccess(deleteSimulationDefinition(id)) { _ => complete(StatusCodes.NoContent)}
          }
        }
    }
  }

  def createSimulationDefinition(sdfr: SimulationDefinitionRequest): Future[Done] = {
    val sdf = SimulationDefinition(
      UUID.randomUUID(),
      sdfr.message,
      ZonedDateTime.now(),
      sdfr.startAt.getOrElse(ZonedDateTime.now()),
      sdfr.endAt.getOrElse(ZonedDateTime.now())
    )
    simulationDefRepo.createSimulationDefinition(sdf)
  }
  def getSimulationDefinitions() = simulationDefRepo.getSimulationDefinitions()
  def fetchSimulation(simulationId: UUID) = simulationDefRepo.getSimulationDefinition(simulationId)
  def patchSimulationDefinition(id: UUID, sdfr: SimulationDefinitionRequest): Future[Done] = simulationDefRepo.updateSimulationDefinition(id, sdfr)
  def deleteSimulationDefinition(id: UUID): Future[Done] = simulationDefRepo.deleteSimulationDefinition(id)

}
