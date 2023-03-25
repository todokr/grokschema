val Version = "0.1.0"

lazy val root = project
  .in(file("."))
  .settings(
    name := "grokschema",
    description := "To understand DB schema thoroughly and intuitively",
    version := Version,
    organization := "io.github.todokr",
    scalaVersion := "3.2.2",
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "0.7.29" % Test
    )
  )
  .settings(
    githubOwner := "todokr",
    githubRepository := "grokschema",
    githubTokenSource := TokenSource.Or(TokenSource.Environment("GITHUB_TOKEN"), TokenSource.GitConfig("github.token"))
  )
