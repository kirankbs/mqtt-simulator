package mqtt.simulator.api.models

import java.time.ZonedDateTime
import java.util.UUID
import spray.json._
import spray.json.DefaultJsonProtocol._
import mqtt.simulator.models.json.Protocols._

/**
  *
  * @param id
  * @param createAt
  * @param updatedAt
  * @param endAt
  */
case class SimulationDefinition(id: UUID,
                                message: String,
                                createAt: ZonedDateTime,
                                updatedAt: ZonedDateTime,
                                endAt: ZonedDateTime)

object SimulationDefinition {
  implicit val format: RootJsonFormat[SimulationDefinition] = jsonFormat5(SimulationDefinition.apply)
}
