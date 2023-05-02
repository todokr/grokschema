ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/todokr/grokschema"),
    "scm:git@github.com:todokr/grokschema.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id = "todokr",
    name = "Shunsuke Tadokoro",
    email = "s.tadokoro0317@gmail.com",
    url = url("https://github.com/todokr/grokschema")
  )
)
ThisBuild / licenses := List("EPL 2.0" -> url("https://www.eclipse.org/legal/epl-2.0/"))
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value) Some("snapshots" at nexus + "content/repositories/snapshots")
  else Some("releases" at nexus + "service/local/staging/deploy/maven2")
}
ThisBuild / publishMavenStyle := true
