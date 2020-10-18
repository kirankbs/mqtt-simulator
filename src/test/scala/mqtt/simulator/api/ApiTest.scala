package mqtt.simulator.api

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField
import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}
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
  val bbb: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  val message = "foo"
  val routes = new API(mockRepo, () => uuid, () => now).routes
  val simulationDefReq = SimulationDefinitionRequest(message, None, None)
  val sdf = SimulationDefinition(uuid, message, now, now, now)
  val nowExpected = LocalDateTime.parse(now.format(bbb), bbb).atZone(ZoneOffset.UTC)
  val sdfExpected = SimulationDefinition(uuid, message, nowExpected, nowExpected, nowExpected)

  "POST /simulation" should {
    "create simulation definition" in {
      mockRepo.createSimulationDefinition(sdf) returns Future.successful(Done)
      Post("/simulation", simulationDefReq) ~> routes ~> check {
        println(response)
        status shouldBe Created
      }
    }
  }
  "Get /simulation" should {
    "return empty result" in {
      mockRepo.getSimulationDefinitions() returns Future.successful(Seq.empty)
      Get("/simulation") ~> routes ~> check {
        status shouldBe OK
        responseAs[Seq[SimulationDefinition]] shouldBe Seq.empty
      }
    }
    "return non empty result" in {
      val result = Seq(sdfExpected, sdfExpected)
      mockRepo.getSimulationDefinitions() returns Future.successful(result)
      Get("/simulation") ~> routes ~> check {
        status shouldBe OK
        responseAs[Seq[SimulationDefinition]] shouldBe result
      }
    }
  }
}
