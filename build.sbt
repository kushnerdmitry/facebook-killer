import java.io.File

name := "facebook-killer"

version := "0.1"

scalaVersion := "2.12.10"

val http4sVersion = "0.21.9"
val doobieVersion = "0.5.0"
val circeVersion = "0.13.0"
val dockerTestkitVersion = "0.9.5"
val pureConfigVersion = "0.12.2"

enablePlugins(AshScriptPlugin, DockerComposePlugin)

dockerBaseImage := "openjdk:8-jre-alpine"

dockerImageCreationTask := (publishLocal in Docker).value


variablesForSubstitution in IntegrationTest := Map(
  "POSTGRES_DB" -> "database_test"
)

parallelExecution in Test := false

lazy val root = project
  .in(file("."))
  .configs(IntegrationTest)
  .settings(
    Defaults.itSettings,
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.1" % "it,test,"
  )

testCasesPackageTask := (sbt.Keys.packageBin in IntegrationTest).value

testCasesJar := artifactPath.in(IntegrationTest, packageBin)
                            .value
                            .getAbsolutePath

testDependenciesClasspath := {
  val fullClasspathCompile = (fullClasspath in Compile).value
  val classpathTestManaged = (managedClasspath in IntegrationTest).value
  val classpathTestUnmanaged = (unmanagedClasspath in IntegrationTest).value
  val testResources = (resources in IntegrationTest).value
  (fullClasspathCompile.files ++ classpathTestManaged.files ++ classpathTestUnmanaged.files ++ testResources)
    .map(_.getAbsoluteFile)
    .mkString(File.pathSeparator)
}


libraryDependencies ++= Seq(

  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion,
  "com.github.pureconfig" %% "pureconfig-enumeratum" % pureConfigVersion,

  "org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "rho-swagger" % "0.21.0-RC1",

  "org.tpolecat" %% "doobie-core" % doobieVersion,
  "org.tpolecat" %% "doobie-hikari" % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.postgresql" % "postgresql" % "42.1.4",
  "org.flywaydb" % "flyway-core" % "6.2.0",

  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,

  "org.webjars" % "swagger-ui" % "3.36.1",

  "com.h2database" % "h2" % "1.4.196" % Test,
  "org.tpolecat" %% "doobie-scalatest" % doobieVersion % Test,
  "org.http4s" %% "http4s-blaze-client" % http4sVersion % IntegrationTest,

  "com.whisk" %% "docker-testkit-scalatest" % dockerTestkitVersion % Test,
  "com.whisk" %% "docker-testkit-impl-spotify" % dockerTestkitVersion % Test
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "utf-8",
  "-explaintypes",
  "-feature",
  "-Ypartial-unification",
  "-Ywarn-extra-implicit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused:implicits",
  "-Ywarn-unused:imports",
  "-Ywarn-unused:locals",
  "-Ywarn-unused:params",
  "-Ywarn-unused:patvars",
  "-Ywarn-unused:privates",
  "-Ywarn-value-discard",
  "-language:higherKinds"
)