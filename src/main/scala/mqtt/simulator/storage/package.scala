package mqtt.simulator

import slick.lifted.TableQuery

package object storage {

  val SimulationDefinitions = TableQuery[SimulationDefinitionTable]

}
