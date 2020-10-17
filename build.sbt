import com.typesafe.sbt.packager.docker.Cmd

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

enablePlugins(JavaAppPackaging)
Docker / packageName := (ThisProject / name).value
Docker / version := (ThisProject / version).value
dockerBaseImage := "anapsix/alpine-java"
dockerExposedPorts ++= Seq(8080)
dockerEntrypoint := Seq("/opt/docker/bin/lunch-o", "-o", "--", s"/opt/docker/bin/${(ThisProject / name).value}")
dockerCmd := Seq("-Dpidfile.path=/dev/null")
dockerCommands += Cmd("ENV", s"SERVICE=${(Docker / name).value}")
