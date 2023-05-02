sonatypeProfileName := "io.github.todokr"
publishMavenStyle := true
licenses += ("EPL 2.0", url("https://www.eclipse.org/legal/epl-2.0/"))
import xerial.sbt.Sonatype._
sonatypeProjectHosting := Some(GitHubHosting("todokr", "grokschema", "s.tadokoro0317@gmail.com"))
scmInfo := Some(
  ScmInfo(url("https://github.com/todokr/grokschema"), "scm:git@github.com:todokr/grokschema.git")
)
developers := List(
  Developer(
    id = "todokr",
    name = "Shunsuke Tadokoro",
    email = "s.tadokoro0317@gmail.com",
    url = url("https://github.com/todokr")
  )
)
