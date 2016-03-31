package util.types

import chitchat.types.range._
import chitchat.types.encoding._
import chitchat.types._
import util.file.Util._

import java.lang.{String => JString}
import scala.collection.mutable.{Map => MMap}

object Util {

  def getSystemTypeInstances() = {
    Map[JString, chitchat.types.Base[_]](
      // range
      "age" -> new Age,
      "level" -> new Level,
      "boolean" -> new Boolean,
      "byte" -> new Byte,
      "ubyte" -> new UByte,

      // encoded
      "date" -> new Date,
      "time" -> new Time,
      "latitude" -> new Latitude,
      "longitude" -> new Longitude,

      // floating
      "float" -> new Float,

      // string
      "string" -> new String
    )
  }

  def getUserTypeInstances(directory: JString, namespace: JString = "chitchat.types") = {
    // val directory = "./src/test/resources/util/file/"
    val instances = getClassInstances[chitchat.types.Base[_]](directory, namespace)
    val mmap = MMap[JString, chitchat.types.Base[_]]()
    instances.foreach {instance =>
      mmap(instance.name) = instance
    }
    mmap
  }

  def getTypeInstances(directory:JString) = {
    getSystemTypeInstances() ++ getUserTypeInstances(directory)
  }
}
