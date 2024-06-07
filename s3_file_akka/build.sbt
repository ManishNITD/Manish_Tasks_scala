name := "s3_file_akka"

version := "0.1"

scalaVersion := "2.13.12"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % "2.6.19",
  "com.typesafe.akka" %% "akka-stream" % "2.6.19",
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "software.amazon.awssdk" % "s3" % "2.20.66",
  "ch.qos.logback" % "logback-classic" % "1.2.11"
)

resolvers += "Akka Repository" at "https://repo.akka.io/releases/"
