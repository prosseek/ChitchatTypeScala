package util.types

import java.lang.{String => JString}

import chitchat.types.Base

import scala.collection.mutable.ArrayBuffer

class TypeInference(val typesDirectory : JString = "") {

  def rangeTypes = Array[java.lang.String]("age", "level", "boolean", "byte", "ubyte")
  def encodingTypes = Array[java.lang.String]("date", "time", "latitude", "longitude")
  val types: Map[java.lang.String, chitchat.types.Base[_]] = Util.getTypeInstances(typesDirectory)

  def getType(label:JString, value:Array[Byte] = null) : Option[Base[_]] = {
    // If we know the type from the name, we return it
    if (types.keySet.contains(label))
      return Some(types(label))

    // If we can guess the value type, we return it
    if (value != null) {
      val result = getTypeFromValue(value)
      if (result.size > 0)
        return Some(types(result(0)))
    }

    None
  }

  def getTypeFromValue(value:Array[Byte]) : Seq[java.lang.String] = {
    // check if the value can be interpreted as string
    val result = ArrayBuffer[java.lang.String]()

    val decodedValue1 = types("string").asInstanceOf[chitchat.types.String].decode(value)
    if (decodedValue1.isDefined) result += "string"

    val decodedValue2 = types("float").asInstanceOf[chitchat.types.Float].decode(value)
    if (decodedValue2.isDefined) result += "float"

    rangeTypes.foreach { range =>
      val decodedValue = types(range).asInstanceOf[chitchat.types.Range].decode(value)
      if (decodedValue.isDefined) result += range
    }

    encodingTypes.foreach { encoding =>
      val decodedValue = types(encoding).asInstanceOf[chitchat.types.Encoding].decode(value)
      if (decodedValue.isDefined) result += encoding
    }

    result.toList
  }
}
