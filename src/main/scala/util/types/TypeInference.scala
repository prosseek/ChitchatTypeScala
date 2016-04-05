package util.types

import java.lang.{String => JString, Float => JFloat}
import scala.{Int => SInt, Byte => SByte}
import scala.collection.mutable.{ListBuffer => LBuffer}

import chitchat.types._
import chitchat.types.range._
import chitchat.types.encoding._

import scala.collection.mutable.ArrayBuffer

class TypeInference(val typeInstances : Map[JString, Base[_]]) {

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

  def getChitchatTypeFromLabel(label:JString) : Option[(JString, Base[_])] = {

    val types = Map(stringTypes   -> "string",
                    floatingTypes -> "float",
                    encodingTypes -> "encoding",
                    rangeTypes    -> "range")

    val typeName = types find (_._1 contains label) map (_._2) getOrElse ""

    if (typeName == "")
      None
    else {
      Some(typeName, typeInstances(label))
    }
  }

  def getTypeFromAnyValue(value:Any) : Seq[JString] = {
    val res = LBuffer[JString]()
    if (value.isInstanceOf[JString]) {
      res += "string"
    }

    res.toSeq
  }

//  def getTypeFromByteArrayValue(value:Array[Byte]) : Seq[java.lang.String] = {
//    // check if the value can be interpreted as string
//    val result = ArrayBuffer[java.lang.String]()
//
//    val decodedValue1 = types("string").asInstanceOf[chitchat.types.String].decode(value)
//    if (decodedValue1.isDefined) result += "string"
//
//    val decodedValue2 = types("float").asInstanceOf[chitchat.types.Float].decode(value)
//    if (decodedValue2.isDefined) result += "float"
//
//    rangeTypes.foreach { range =>
//      val decodedValue = types(range).asInstanceOf[chitchat.types.Range].decode(value)
//      if (decodedValue.isDefined) result += range
//    }
//
//    encodingTypes.foreach { encoding =>
//      val decodedValue = types(encoding).asInstanceOf[chitchat.types.Encoding].decode(value)
//      if (decodedValue.isDefined) result += encoding
//    }
//
//    result.toList
//  }
}
