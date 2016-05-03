name := "chitchattype"

version := "0.1"
scalaVersion := "2.11.7"

target in Compile in doc := baseDirectory.value / "doc/api"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value

libraryDependencies += "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.2"

//(fullClasspath in Test) += Attributed.blank(file("./src/test/resources/util/file/"))