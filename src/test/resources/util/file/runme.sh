#!/bin/bash

JAR_LOCATION="../../../../../target/scala-2.11/chitchattypescala_2.11-0.1.jar"

scalac -cp $JAR_LOCATION:. a.scala
scalac -cp $JAR_LOCATION:. b.scala
scalac -cp $JAR_LOCATION:. c.scala
