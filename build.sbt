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
      "org.scalameta" %% "munit" % "0.7.29" % "it,test",
      "org.postgresql" % "postgresql" % "42.2.23" % "it"
    ),
    Defaults.itSettings
  )
  .settings(GithubPublishSetting)
  .configs(IntegrationTest)

lazy val GithubPublishSetting = {
  if (sys.env.contains("GITHUB_TOKEN")) {
    Seq(
      githubOwner := "todokr",
      githubRepository := "grokschema",
      githubTokenSource := TokenSource.Environment("GITHUB_TOKEN")
    )
  } else Seq.empty
}
