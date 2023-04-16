ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "openai-scala-client",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % "2.0.1",
      "dev.zio" %% "zio-streams" % "2.0.1",
      "dev.zio" %% "zio-logging" % "2.0.1",
      "com.softwaremill.sttp.client4" %% "zio" % "4.0.0-M1", // for ZIO 2.x
      "com.softwaremill.sttp.client4" %% "circe" % "4.0.0-M1",
      "com.softwaremill.retry" %% "retry" % "0.3.6",
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "dev.zio" %% "zio-test" % "2.0.12" % Test,
      "dev.zio" %% "zio-mock" % "1.0.0-RC10" % Test
    )
  )
