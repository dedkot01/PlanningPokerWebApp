val ScalatraVersion = "2.8.0"

ThisBuild / scalaVersion := "2.13.4"
ThisBuild / organization := "org.dedkot"

lazy val planningPoker = (project in file("."))
  .settings(
    name := "PlanningPokerWebApp",
    version := "0.1",
    libraryDependencies ++= Seq(
      "org.scalatra" %% "scalatra" % ScalatraVersion,
      "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
      "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
      "org.eclipse.jetty" % "jetty-webapp" % "9.4.35.v20201120" % "container",
      "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided"
    ),
  )

enablePlugins(SbtTwirl)
enablePlugins(JettyPlugin)
