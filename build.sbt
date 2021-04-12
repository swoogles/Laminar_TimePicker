name := "TimePicker"
version := "0.2.2"
organization := "com.billdingsoftware"

enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.12.1",
  "com.raquo" %%% "airstream" % "0.12.0",
  "org.scala-js" %%% "scalajs-dom" % "1.1.0",
  "com.lihaoyi" %%% "utest" % "0.7.5" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")

// POM settings for Sonatype
homepage := Some(url("https://github.com/swoogles/Laminar_TimePicker"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/swoogles/Laminar_TimePicker"),
    "git@github.com:swoogles/Laminar_TimePicker.git")
)
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true