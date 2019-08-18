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

val http4sVersion = "0.20.0-M5"
//val zioVersion     = "1.0.0-RC10-1"
val zioVersion     = "1.0.0-RC9"
val interopVersion = "1.0.0-RC8-10"

addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")

lazy val root =
  (project in file("."))
    .settings(
      stdSettings("metrics")
    )

libraryDependencies ++= Seq(
  "dev.zio"    %% "zio"                  % zioVersion,
  "dev.zio"    %% "zio-interop-cats"     % interopVersion,
  "dev.zio"    %% "zio-interop-scalaz7x" % interopVersion,
  "org.scalaz" % "testz-core_2.12"       % "0.0.5",
  "org.scalaz" % "testz-stdlib_2.12"     % "0.0.5"
)

libraryDependencies ++= Seq(
  "io.dropwizard.metrics" % "metrics-core"         % "4.0.1",
  "io.dropwizard.metrics" % "metrics-healthchecks" % "4.0.1",
  "io.dropwizard.metrics" % "metrics-jmx"          % "4.0.1"
)

libraryDependencies += "io.prometheus" % "simpleclient" % "0.6.0"

libraryDependencies ++= Seq(
  //"org.http4s" %% "http4s-blaze-client" % http4sVersion,
  //"org.http4s" %% "http4s-circe" % http4sVersion,
  "org.http4s" %% "http4s-argonaut"     % http4sVersion,
  "org.http4s" %% "http4s-blaze-server" % http4sVersion,
  "org.http4s" %% "http4s-dsl"          % http4sVersion
)

libraryDependencies ++= Seq(
  "io.argonaut" %% "argonaut"        % "6.2.2",
  "io.argonaut" %% "argonaut-scalaz" % "6.2.2"
)

scalacOptions ++= (CrossVersion.partialVersion(scalaBinaryVersion.value) match {
  case Some((2, 11)) => Seq("-Ypartial-unification", "-Ywarn-value-discard", "-target:jvm-1.8")
  case _             => Seq("-Ypartial-unification", "-Ywarn-value-discard")
})

addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.7")

// TODO: enforce scalazzi dialect through the scalaz-plugin
// addCompilerPlugin("org.scalaz" % "scalaz-plugin_2.12.4" % "0.0.7")
