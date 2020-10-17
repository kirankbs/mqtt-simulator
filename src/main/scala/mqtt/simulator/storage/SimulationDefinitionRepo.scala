package mqtt.simulator.storage

import java.util.UUID

import akka.Done
import mqtt.simulator.api.models.{SimulationDefinition, SimulationDefinitionRequest}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import CustomPostgresProfile.api._

class SimulationDefinitionRepo(database: Database) {
  def createSimulationDefinition(sd: SimulationDefinition): Future[Done] =
    database.run(simulationDefinitions += sd).map(_ => Done)
  def getSimulationDefinition(simulationId: UUID) =
    database.run(simulationDefinitions.filter(_.id === simulationId).take(1).result.headOption)
  def getSimulationDefinitions() = database.run(simulationDefinitions.result)
  def updateSimulationDefinition(id: UUID, sdfr: SimulationDefinitionRequest): Future[Done] =
    database.run(simulationDefinitions.filter(_.id === id).map(sdf => sdf.message).update(sdfr.message)).map(_ => Done)
  def deleteSimulationDefinition(id: UUID) =
    database.run(simulationDefinitions.filter(_.id === id).delete).map(_ => Done)
}
