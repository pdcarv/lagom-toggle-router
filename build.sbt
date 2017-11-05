organization in ThisBuild := "com.xpto"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.11.8"

lazy val `xpto` = (project in file("."))
  .aggregate(`toggle-api`, `toggle-impl`, `abc-api`, `abc-impl`)

lazy val `toggle-api` = (project in file("toggle-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `toggle-impl` = (project in file("toggle-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomJavadslTestKit,
      lombok,
      cassandraExtras,
      assertj
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`toggle-api`)

lazy val `abc-api` = (project in file("abc-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lagomJavadslJackson,
      lombok
    )
  )

lazy val `abc-impl` = (project in file("abc-impl"))
  .settings(lagomForkedTestSettings: _*)
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslPubSub,
      lagomJavadslPersistenceCassandra,
      lagomJavadslKafkaBroker,
      lagomJavadslTestKit,
      lombok,
      assertj
    )
  )
  .dependsOn(`abc-api`, `toggle-api`)

val lombok = "org.projectlombok" % "lombok" % "1.16.10"
val cassandraExtras = "com.datastax.cassandra" % "cassandra-driver-extras" % "3.0.0"
val assertj = "org.assertj" % "assertj-core" % "3.8.0"


def common = Seq(
  javacOptions in compile += "-parameters"
)

lagomCassandraCleanOnStart in ThisBuild := false



