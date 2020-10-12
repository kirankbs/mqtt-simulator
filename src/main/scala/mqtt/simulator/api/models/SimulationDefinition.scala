package mqtt.simulator.api.models

import java.time.ZonedDateTime
import java.util.UUID
import spray.json.DefaultJsonProtocol.jsonFormat5
import spray.json._
import spray.json.DefaultJsonProtocol._
import mqtt.simulator.models.json.Protocols._

/**
 *
 * @param id
 * @param createAt
 * @param startAt
 * @param updatedAt
 * @param endAt
 */
case class SimulationDefinition(
                                 id: UUID,
                                 createAt: ZonedDateTime,
                                 startAt: Option[ZonedDateTime],
                                 updatedAt: Option[ZonedDateTime],
                                 endAt: Option[ZonedDateTime]
                               )

object SimulationDefinition {
  implicit val format: RootJsonFormat[SimulationDefinition] = jsonFormat5(SimulationDefinition.apply)
}
