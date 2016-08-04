name := "GetXcodeAppliPath"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq(
  "-deprecation", "-feature", "-unchecked", "-Xlint:-missing-interpolator", "-Xfatal-warnings"
)

libraryDependencies ++= Seq(
  //"org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.1",
  //"org.scala-lang.plugins" %% "scala-continuations-library" % "1.0.1",
  "org.scala-lang.modules" %% "scala-xml" %  "1.0.1",
  "com.github.pathikrit"   %% "better-files" % "2.16.0"
)
