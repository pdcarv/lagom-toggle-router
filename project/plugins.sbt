// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.3.9")
// Needed for importing the project into Eclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.1.0")

resolvers += Resolver.url("bintray.scaffolding-plugin.resolver", url("http://dl.bintray.com/fabszn/sbt-plugins"))(Resolver.ivyStylePatterns)

addSbtPlugin("com.lightbend.lagom.sbt" %% "scaffolding-plugin-lagom" % "1.0.1-M1")