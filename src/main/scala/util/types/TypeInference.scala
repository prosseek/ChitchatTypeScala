package util.types

import java.lang.{String => JString, Float => JFloat}
import scala.{Int => SInt, Byte => SByte}
import scala.collection.mutable.{ListBuffer => LBuffer}

import chitchat.types._
import chitchat.types.range._
import chitchat.types.encoding._

import scala.collection.mutable.ArrayBuffer

object TypeInference {
  def apply(directory:JString = "") = {
    val typeInstances = Util.getTypeInstances(directory)
    new TypeInference(typeInstances)
  }
}

class TypeInference(val typeInstances : Map[JString, Base[_]]) {
  // Internal data structure
  val encodingTypes = ArrayBuffer[JString]("date", "time", "latitude", "longitude")
  val rangeTypes = ArrayBuffer[JString]("age", "level", "boolean", "byte", "ubyte")
  val floatingTypes = ArrayBuffer[JString]("float")
  val stringTypes = ArrayBuffer[JString]("string")

  // from user's types
  typeInstances foreach {
    case (name, base_type) => {
      if (name.startsWith("encoding_")) {
        encodingTypes += name
      }
      else if (name.startsWith("range_")) {
        rangeTypes += name.replace("range_", "")
      }
      else if (name.startsWith("float_")) {
        floatingTypes += name.replace("float_", "")
      }
      else if (name.startsWith("string_")) {
        stringTypes += name.replace("string_", "")
      }
    }
  }

  def encodeFromLabel(input:JString, value:Any) : Option[Array[SByte]] = {
    if (stringTypes.contains(input))
      Some(typeInstances("string").asInstanceOf[String].encode(value.asInstanceOf[JString]))
    else if (floatingTypes.contains(input))
      Some(typeInstances("float").asInstanceOf[Float].encode(value.asInstanceOf[JFloat]))
    else if (encodingTypes.contains(input))
      Some(typeInstances(input).asInstanceOf[Encoding].encode(value.asInstanceOf[Seq[SInt]]))
    else if (rangeTypes.contains(input))
      Some(typeInstances(input).asInstanceOf[Range].encode(value.asInstanceOf[SInt]))
    else
      None
  }

  def getChitchatTypeFromLabel(label:JString) : Option[Base[_]] = {

    val types = Map(stringTypes   -> "string",
                    floatingTypes -> "float",
                    encodingTypes -> "encoding",
                    rangeTypes    -> "range")

    val typeName = types find (_._1 contains label) map (_._2) getOrElse ""

    if (typeName == "")
      None
    else {
      Some(typeInstances(label))
    }
  }

  def getTypeFromAnyValue(value:Any) : Seq[JString] = {
    val res = LBuffer[JString]()
    if (value.isInstanceOf[JString]) {
      res += "string"
    }
    res
  }
}
