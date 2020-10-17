package mqtt.simulator.storage

import slick.jdbc.PostgresProfile

trait CustomPostgresProfile extends PostgresProfile with BaseColumnMapping {

  override val api = Api

  object Api extends API with DateTimeColumnMapping
}
object CustomPostgresProfile extends CustomPostgresProfile