package mqtt.simulator.api.models

import mqtt.simulator.models.MessageDefinition

case class SimulationRequestPost(messages: List[MessageDefinition])