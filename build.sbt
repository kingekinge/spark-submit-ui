name := "spark-submit-ui"

version := "v1.01

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.apache.commons" % "commons-email" % "1.4",
  "org.apache.spark" % "spark-core_2.10" % "1.4.1",
  "com.google.inject" % "guice" % "3.0",
  "com.tzavellas" % "sse-guice" % "0.7.1",
  "com.jolbox" % "bonecp" % "0.8.0.RELEASE"
)

play.Project.playScalaSettings

resolvers ++= Seq(
  "Apache Repository" at "https://repository.apache.org/content/repositories/releases/",
  "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
  Resolver.sonatypeRepo("public")
)

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }




