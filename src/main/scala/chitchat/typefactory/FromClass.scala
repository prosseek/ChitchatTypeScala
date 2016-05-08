package chitchat.typefactory

import java.lang.{String => JString}

import chitchat.types._
import chitchat.types.encoding._
import chitchat.types.range._
import chitchat.types.string._
//import chitchat.types.float._
import util.file.Util._

import scala.collection.mutable.{Map => MMap}

object FromClass {

  def getSystemTypeInstances = {
    Map[JString, chitchat.types.Base[_]](

      // range
      "age" -> new Age,
      "level" -> new Level,
      "boolean" -> new Boolean,
      "byte" -> new Byte,
      "ubyte" -> new UByte,
      "short" -> new Short,
      "ushort" -> new UShort,

      // encoded
      "date" -> new Date,
      "time" -> new Time,
      "latitude" -> new Latitude,
      "longitude" -> new Longitude,

      // floating
      "float" -> new Float,

      // string
      "string" -> new String,
      "name" -> new Name
    )
  }

  def getUserTypeInstances(directory: JString, namespace: JString = "chitchat.types") = {
    // todo: plugin uses only binary or textual summary
    MMap[JString, chitchat.types.Base[_]]()
//    val mmap = MMap[JString, chitchat.types.Base[_]]()
//
//    if (directory == "") {
//      mmap
//    } else {
//      // val directory = "./src/test/resources/util/file/"
//      val instances = getClassInstances[chitchat.types.Base[_]](directory, namespace)
//
//      instances.foreach { instance =>
//        mmap(instance.name) = instance
//      }
//      mmap
//    }
  }

  def getTypeInstances(directory:JString) : Map[JString, chitchat.types.Base[_]] = {
    getSystemTypeInstances ++ getUserTypeInstances(directory)
  }
}
