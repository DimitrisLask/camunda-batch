name := """BatchController"""
organization := "dimitris"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, LauncherJarPlugin, ClasspathJarPlugin)

scalaVersion := "2.13.0"

libraryDependencies += guice

libraryDependencies ++= Seq(
  javaJdbc,
  "mysql" % "mysql-connector-java" % "5.1.46",
  "com.rabbitmq" % "amqp-client" % "3.5.2",
  "org.apache.httpcomponents" % "httpclient" % "4.5.10"
)  
