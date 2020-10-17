package mqtt.simulator.api.models

import java.time.ZonedDateTime
import spray.json.RootJsonFormat
import spray.json.DefaultJsonProtocol._
import mqtt.simulator.models.json.Protocols._

case class SimulationDefinitionRequest(message: String, startAt: Option[ZonedDateTime], endAt: Option[ZonedDateTime])
object SimulationDefinitionRequest {
  implicit val format: RootJsonFormat[SimulationDefinitionRequest] = jsonFormat3(SimulationDefinitionRequest.apply)
}
