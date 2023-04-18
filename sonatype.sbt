// Your profile name of the sonatype account. The default is the same with the organization value
sonatypeProfileName := "com.raisondata.openai"

// To sync with Maven central, you need to supply the following information:
publishMavenStyle := true

// Open-source license of your choice
licenses := Seq("MIT" -> url("https://opensource.org/license/MIT"))

// Where is the source code hosted: GitHub or GitLab?
import xerial.sbt.Sonatype._
homepage := Some(url("https://github.com/ianshiundu/openai-zio"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/ianshiundu/openai-zio"),
    "scm:git@github.com:ianshiundu/openai-zio.git"
  )
)

developers := List(
  Developer(
    id = "com.github.ianshiundu",
    name = "Ian Shiundu",
    email = "ian@raisondata.com",
    url = url("http://raisondata.com")
  )
)
