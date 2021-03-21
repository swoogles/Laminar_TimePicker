
name := "TimePicker"

enablePlugins(ScalaJSPlugin)

scalaJSUseMainModuleInitializer := true

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "com.raquo" %%% "laminar" % "0.12.1",
  "com.raquo" %%% "airstream" % "0.12.0",
)
