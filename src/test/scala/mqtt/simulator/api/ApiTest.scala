package mqtt.simulator.api

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder}
import java.time.temporal.ChronoField
import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}
import java.util.UUID

import akka.Done
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._
import akka.http.scaladsl.model.StatusCodes.{Created, NoContent, OK}
import akka.http.scaladsl.server.Directives.delete
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
  "Get /simulation/<id>" should {
    "return empty result" in {
      mockRepo.getSimulationDefinition(uuid) returns Future.successful(None)
      Get(s"/simulation/$uuid") ~> routes ~> check {
        status shouldBe NoContent
      }
    }
    "return non empty result" in {
      mockRepo.getSimulationDefinition(uuid) returns Future.successful(Some(sdfExpected))
      Get(s"/simulation/$uuid") ~> routes ~> check {
        status shouldBe OK
        responseAs[SimulationDefinition] shouldBe sdfExpected
      }
    }
  }
  "Patch /simulation/<id>" should {
    "update simulation definition" in {
      mockRepo.updateSimulationDefinition(uuid, simulationDefReq) returns Future.successful(Done)
      Patch(s"/simulation/$uuid", simulationDefReq) ~> routes ~> check {
        status shouldBe NoContent
      }
    }
  }
  "Delete /simulation/<id>" should {
    "delete simulation definition" in {
      mockRepo.deleteSimulationDefinition(uuid) returns Future { Done }
      Delete(s"/simulation/$uuid") ~> routes ~> check {
        status shouldBe NoContent
      }
    }
  }
}
