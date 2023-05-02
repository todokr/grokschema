val Version = "0.1.1"
val Scala2 = "2.13.10"
val Scala3 = "3.2.2"

// the default compiler and REPL
ThisBuild / scalaVersion := Scala3

val buildSettings = Seq[Setting[_]](
  crossScalaVersions := Seq(Scala3),
  organization := "io.github.todokr",
  crossPaths := true,
  publishMavenStyle := true,
  Test / logBuffered := false,
  publishTo := sonatypePublishToBundle.value
)

lazy val root = project
  .in(file("."))
  .settings(
    buildSettings,
    name := "grokschema",
    description := "To understand DB schema thoroughly and intuitively",
    version := Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % "it,test",
      "org.postgresql" % "postgresql" % "42.2.23" % "it"
    ),
    Defaults.itSettings
  )
  .configs(IntegrationTest)
