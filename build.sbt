val scala3Version = "3.2.2"

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
  .aggregate(core)

lazy val sandbox = project
  .in(file("sandbox"))
  .settings(
    scalaVersion := scala3Version,
    publishArtifact := false
  )
  .dependsOn(core)

lazy val core = project
  .in(file("core"))
  .settings(
    organization := "io.github.todokr",
    name := "grokschema-core",
    version := "2023.3.0-SNAPSHOT",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq(
      "-new-syntax",
      "-rewrite",
      "-feature",
      "-deprecation",
      "-unchecked"
    ),
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.5.1",
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
  .settings(
    githubOwner := "todokr",
    githubRepository := "grokschema",
    githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")
  )
