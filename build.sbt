
name := "TimePicker"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
//  "com.billding" %%% "brieftime" % "0.0.18",
  "com.raquo" %%% "laminar" % "0.12.1",
  "com.raquo" %%% "airstream" % "0.12.0",
)
