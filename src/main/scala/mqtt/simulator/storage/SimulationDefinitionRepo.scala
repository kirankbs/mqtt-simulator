package mqtt.simulator.storage

import akka.Done
import mqtt.simulator.api.models.SimulationDefinition
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import CustomPostgresProfile.api._

class SimulationDefinitionRepo(database: Database) {

  def createSimulationDefinition(sd: SimulationDefinition): Future[Done] = {
    database.run(simulationDefinitions += sd).map(_ => Done)
  }

}
