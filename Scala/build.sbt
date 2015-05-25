name := "Comparision_Scala"

version := "1.0"

scalaVersion := "2.11.5"

parallelExecution in Test := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.scalacheck" % "scalacheck_2.11" % "1.11.6" % "test",
  "org.seleniumhq.selenium" % "selenium-java" % "2.43.1" % "test"
)