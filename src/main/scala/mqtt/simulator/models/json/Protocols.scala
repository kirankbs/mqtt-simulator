package mqtt.simulator.models.json

import java.time.{ZoneOffset, ZonedDateTime}
import java.time.format.DateTimeFormatter
import java.util.UUID

import spray.json.{JsString, JsValue, RootJsonFormat, deserializationError}

object Protocols {

  implicit val uuidFormat: RootJsonFormat[UUID] = new RootJsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(value) => UUID.fromString(value)
      case str => throw new IllegalArgumentException(s"UUID is not string. Given value is $str")
    }
  }

  implicit val zonedDateTimeFormat = new RootJsonFormat[ZonedDateTime] {
    override def read(json: JsValue): ZonedDateTime = json match {
      case JsString(value) =>
        try {
          ZonedDateTime.parse(value, DateTimeFormatter.ISO_ZONED_DATE_TIME).withZoneSameInstant(ZoneOffset.UTC)
        } catch {
          case x: Exception =>
            deserializationError(s"Date Time Format Exception. Given value is: $value")
        }
      case _ => deserializationError("Expected ISO DateTime string such as '2011-12-03T10:15:30+01:00[Europe/Paris]'")
    }

    override def write(obj: ZonedDateTime): JsValue =
      JsString(obj.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX").withZone(ZoneOffset.UTC)))
  }

}
