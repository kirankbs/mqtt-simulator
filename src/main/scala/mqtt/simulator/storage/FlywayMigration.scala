package mqtt.simulator.storage

import com.typesafe.config.Config
import org.flywaydb.core.Flyway

object FlywayMigration {

  def apply(config: Config, dbPrefix: String): Unit = {
    val serverName = config.getString(s"$dbPrefix.properties.serverName")
    val portNumber = config.getString(s"$dbPrefix.properties.portNumber")
    val databaseName = config.getString(s"$dbPrefix.properties.databaseName")
    val url = s"jdbc:postgresql://$serverName:$portNumber/$databaseName"
    val user = config.getString(s"$dbPrefix.properties.user")
    val password = config.getString(s"$dbPrefix.properties.password")

    val flyway = Flyway.configure().dataSource(url, user, password).load()

    val appliedMigrations = flyway.migrate()
    println(s"Flyway applied $appliedMigrations migrations to database $url")
  }

}
