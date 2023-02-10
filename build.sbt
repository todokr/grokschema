val scala3Version = "3.2.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "Schema Reader",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-unchecked",
    ),
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.5.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
