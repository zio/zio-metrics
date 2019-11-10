import Build._

inThisBuild(
  List(
    organization := "dev.zio",
    homepage := Some(url("https://github.com/zio/zio-metrics/")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer("jdegoes", "John De Goes", "john@degoes.net", url("http://degoes.net")),
      Developer("toxicafunk", "Eric Noam", "toxicafunk@gmail.com", url("https://github.com/toxicafunk"))
    ),
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    releaseEarlyWith := SonatypePublisher,
    scmInfo := Some(
      ScmInfo(url("https://github.com/zio/zio-metrics/"), "scm:git:git@github.com:zio/zio-metrics.git")
    )
  )
)

val http4sVersion  = "0.20.0-M5"
val zioVersion     = "1.0.0-RC14"
val interopVersion = "2.0.0.0-RC5"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val root =
  (project in file("."))
    .aggregate(common, dropwizard, prometheus)
    .settings(settings)
//.disablePlugins(AssemblyPlugin)

lazy val common = project
  .settings(
    name := "common",
    //settings,
    stdSettings("metrics"),
    libraryDependencies ++= commonDependencies
  )
//.disablePlugins(AssemblyPlugin)

lazy val dropwizard = project
  .settings(
    name := "dropwizard",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "io.dropwizard.metrics" % "metrics-core"         % "4.0.1",
      "io.dropwizard.metrics" % "metrics-healthchecks" % "4.0.1",
      "io.dropwizard.metrics" % "metrics-jmx"          % "4.0.1"
    )
  )
  .dependsOn(common)

lazy val prometheus = project
  .settings(
    name := "prometheus",
    settings,
    libraryDependencies ++= commonDependencies ++ Seq(
      "io.prometheus" % "simpleclient"         % "0.7.0",
      "io.prometheus" % "simpleclient_hotspot" % "0.7.0",
      "io.prometheus" % "simpleclient_common"  % "0.7.0"
    )
  )
  .dependsOn(common)

lazy val commonDependencies = Seq(
  "dev.zio"    %% "zio"              % zioVersion,
  "dev.zio"    %% "zio-interop-cats" % interopVersion,
  "org.scalaz" % "testz-core_2.12"   % "0.0.5",
  "org.scalaz" % "testz-stdlib_2.12" % "0.0.5"
)

lazy val settings = stdSettings("metrics") ++ Seq(
  scalacOptions ++= (CrossVersion.partialVersion(scalaBinaryVersion.value) match {
    case Some((2, 11)) => Seq("-Ypartial-unification", "-Ywarn-value-discard", "-target:jvm-1.8")
    case _             => Seq("-Ypartial-unification", "-Ywarn-value-discard")
  })
)

libraryDependencies ++= Seq(
  //"org.http4s" %% "http4s-blaze-client" % http4sVersion,
  //"org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s"    %% "http4s-argonaut"     % http4sVersion,
  "org.http4s"    %% "http4s-blaze-server" % http4sVersion,
  "org.http4s"    %% "http4s-dsl"          % http4sVersion,
  "org.typelevel" %% "cats-effect"         % "2.0.0" % Optional
)

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut"        % "6.2.2",
  "io.argonaut" %% "argonaut-scalaz" % "6.2.2"
)

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")

// TODO: enforce scalazzi dialect through the scalaz-plugin
// addCompilerPlugin("org.scalaz" % "scalaz-plugin_2.12.4" % "0.0.7")
