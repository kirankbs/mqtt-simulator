name := "mqtt-simulator"
scalaVersion := "2.13.2"


//AKkA Stack
val AkkaVersion = "2.6.8"
val AkkaHttpVersion = "10.2.1"
val SlickVersion = "3.3.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.slick" %% "slick" % SlickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion,
  "org.flywaydb" % "flyway-core" % "7.0.2",
  "org.postgresql" % "postgresql" % "9.4-1206-jdbc42",
  "com.github.tminglei" %% "slick-pg" % "0.19.3"
)

scalafmtOnCompile := true


