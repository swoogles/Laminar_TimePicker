name := "TimePicker"
version := "0.3.15"
organization := "com.billdingsoftware"

enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true

scalaVersion := "3.3.1"

val zioVersion = "2.0.17"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "16.0.0",
  "com.raquo" %%% "airstream" % "16.0.0",
  "org.scala-js" %%% "scalajs-dom" % "2.6.0",
  "io.github.kitlangton" %%% "animus" % "0.3.4",
  "com.lihaoyi" %%% "utest" % "0.8.1" % "test",
  "dev.zio" %%% "zio-test" % zioVersion % "test",
  "dev.zio" %%% "zio-test-sbt" % zioVersion % "test",
  "dev.zio" %%% "zio-json" % "0.6.2",
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
//publishMavenStyle := true

//publishTo := (
//  if (isSnapshot.value)
//    Opts.resolver.sonatypeOssSnapshots.headOption
//  else
//    Some(Opts.resolver.sonatypeStaging)
//)

githubOwner := "swoogles"
githubRepository := "Laminar_TimePicker"
