
name := "TimePicker"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true

version := "0.1.5"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.12.1",
  "com.raquo" %%% "airstream" % "0.12.0",
//  "org.scalameta" %%% "munit" % "0.7.22" % Test,
  "org.scala-js" %%% "scalajs-dom" % "1.1.0",

  "com.lihaoyi" %%% "utest" % "0.7.5" % "test"
)

testFrameworks += new TestFramework("utest.runner.Framework")
// Use %%% for non-JVM projects.
//testFrameworks += new TestFramework("munit.Framework")
//Test / scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) }

// POM settings for Sonatype

organization := "com.billdingsoftware"

homepage := Some(url("https://github.com/swoogles/Laminar_TimePicker"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/swoogles/Laminar_TimePicker"),
    "git@github.com:swoogles/Laminar_TimePicker.git")
)
developers := List(Developer(
  id = "bfrasure",
  name = "Bill Frasure",
  email = "bill@billdingsoftware.com",
  url = url("https://github.com/swoogles")
))
licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
publishMavenStyle := true

// Add sonatype repository settings
/*

publishTo := Some(
  if (isSnapshot.value)
    Opts.resolver.sonatypeSnapshots
  else
    Opts.resolver.sonatypeStaging
)
 */
publishTo := sonatypePublishToBundle.value
ThisBuild / sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"

Global / onChangedBuildSource := ReloadOnSourceChanges

/* TODO Needed?
credentials += Credentials(
  "GnuPG Key ID",
  "gpg",
  "", // key identifier
  "ignored" // this field is ignored; passwords are supplied by pinentry
)

 */