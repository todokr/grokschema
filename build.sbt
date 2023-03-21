ThisBuild / version := "2023.3.0"

ThisBuild / scalaVersion := "3.2.2"

val NoPublish = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {},
  publish / skip := true,
  crossScalaVersions := Nil,
  Compile / doc / sources := Seq.empty,
  Compile / packageDoc / publishArtifact := false
)

lazy val root = project
  .in(file("."))
  .settings(NoPublish)
  .settings(
    name := "root"
  )
  .aggregate(core)

lazy val sandbox = project
  .in(file("sandbox"))
  .settings(
    name := "sandbox",
    publishArtifact := false,
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.5.1"
    )
  )
  .dependsOn(core)

lazy val core = project
  .in(file("core"))
  .settings(
    name := "grokschema-core",
    description := "To understand DB schema thoroughly and intuitively",
    organization := "io.github.todokr",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
  .settings(
    githubOwner := "todokr",
    githubRepository := "grokschema",
    githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")
  )
