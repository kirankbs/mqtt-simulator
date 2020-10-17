package mqtt.simulator.storage

import java.time.{LocalDateTime, ZoneOffset, ZonedDateTime}
import com.github.tminglei.slickpg.PgDate2Support
import slick.jdbc.{JdbcType, PostgresProfile}

trait BaseColumnMapping extends PgDate2Support { this: PostgresProfile =>

  trait DateTimeColumnMapping extends DateTimeImplicits {
    val toZonedDateTime: String => ZonedDateTime = LocalDateTime.parse(_, date2DateTimeFormatter).atZone(ZoneOffset.UTC)
    val fromZonedDateTime: ZonedDateTime => String =
      _.withZoneSameInstant(ZoneOffset.UTC).format(date2DateTimeFormatter)

    implicit override val date2TzTimestamp1TypeMapper: JdbcType[ZonedDateTime] =
      new GenericJdbcType[ZonedDateTime]("timestamp", toZonedDateTime, fromZonedDateTime)
  }
}