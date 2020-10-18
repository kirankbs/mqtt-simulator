package mqtt.simulator.api

import java.time.ZonedDateTime
import java.util.UUID

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.model.StatusCodes.{Created, OK}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import mqtt.simulator.api.models.{SimulationDefinition, SimulationDefinitionRequest}
import mqtt.simulator.storage.SimulationDefinitionRepo
import org.mockito.IdiomaticMockito
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

class ApiTest extends AnyWordSpec with Matchers with IdiomaticMockito with ScalatestRouteTest {

  val mockAPi = mock[ApiTest]
  val mockRepo = mock[SimulationDefinitionRepo]
  val uuid = UUID.randomUUID()
  val now = ZonedDateTime.now()
  val message = "foo"
  val routes = new API(mockRepo, () => uuid, () => now).routes
  val simulationDefReq = SimulationDefinitionRequest(message, None, None)

  "POST /simulation" should {
    "create simulation definition" in {
      val sdf = SimulationDefinition(uuid, message, now, now, now)
      mockRepo.createSimulationDefinition(sdf) returns Future.successful(Done)
      Post("/simulation", simulationDefReq) ~> routes ~> check {
        println(response)
        status shouldBe Created
      }
    }
  }
}
