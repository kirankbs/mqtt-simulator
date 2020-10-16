package mqtt.simulator.storage

import java.time.ZonedDateTime
import java.util.UUID
import mqtt.simulator.api.models.SimulationDefinition
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class SimulationDefinitionTable(tag: Tag) extends Table[SimulationDefinition](tag, "simulation_definitions") {

  def id = column[UUID]("id", O.PrimaryKey)
  def message = column[String]("message")
  def createAt = column[ZonedDateTime]("create_at")
  def updateAt = column[ZonedDateTime]("update_at")
  def endAt = column[ZonedDateTime]("end_at")

  override def * =
    (id, message, createAt, updateAt, endAt).<>((SimulationDefinition.apply _).tupled, SimulationDefinition.unapply)
}
