import sbt.Keys._

name := "index-akka"

version := "1.0"

scalaVersion := "2.11.4"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= {
  val akkaVersion = "2.3.7"
  val sprayVersion = "1.3.1"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % "1.0-M1"
  )
}

Revolver.settings

javaOptions in Revolver.reStart += "-Dfile.encoding=utf8"
