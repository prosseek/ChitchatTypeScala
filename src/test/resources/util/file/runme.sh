#!/bin/bash

JAR_LOCATION="../../../../../target/scala-2.11/chitchattype_2.11-0.1.jar"
#JAR_LOCATION="/Users/smcho/github/ChitchatTypeScala/target/scala-2.11/chitchattype_2.11-0.1.jar"
scalac -cp $JAR_LOCATION:. a.scala
scalac -cp $JAR_LOCATION:. b.scala
scalac -cp $JAR_LOCATION:. c.scala
scalac -cp $JAR_LOCATION:. d.scala
scalac -cp $JAR_LOCATION:. e.scala
scalac -cp $JAR_LOCATION:. f.scala