import com.typesafe.config._
import sbt.Keys.{description, javaOptions, libraryDependencies}

// Read the name and version from the config file (so that it can also be read by the app at run time)
val conf = ConfigFactory.parseFile(new File("graphql-demo/conf/application.conf")).resolve()

lazy val graphqlDemo = (project in file("graphql-demo"))
  .enablePlugins(PlayScala)
  .settings(
    name := "graphql-demo",
    version := "1.0",
    scalaVersion := "2.11.11",
    description := "GraphQL back-end demo",
    fork in run := true,

    scalacOptions ++= Seq("-unchecked", "-feature"),

    // Global UTF-8
    javaOptions += "-Dfile.encoding=UTF8",

    resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/maven-releases/",

    libraryDependencies ++= Seq(
      cache,
      ws,
      filters,

      // webservice client for HTTP
      "com.typesafe.play" % "play-ws_2.11" % "2.5.10",

      // GraphQL library
      "org.sangria-graphql" % "sangria_2.11" % "1.0.0",

      // support for Play JSON in Sangria
      "org.sangria-graphql" %% "sangria-play-json" % "1.0.0",

      // ORM
      "com.typesafe.play" %% "play-slick" % "2.0.1",

      // For migrations
      "com.typesafe.play" %% "play-slick-evolutions" % "2.0.1",

      // MS SQL Driver for Slick
      "com.typesafe.slick" %% "slick-extensions" % "3.1.0"
    )
  )

lazy val root = (project in file(".")).aggregate(graphqlDemo)
