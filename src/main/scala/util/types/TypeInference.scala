package util.types

import scala.collection.mutable.ArrayBuffer

class TypeInference(val typesDirectory : String = "") {

  val types: Map[java.lang.String, chitchat.types.Base[_]] = Util.getTypeInstances(typesDirectory)

  def getType(label:String, value:Array[Byte] = null) = {
    if (value != null) {
      getTypeFromValue(value)
    }
  }

  private def getTypeFromValue(value:Array[Byte]) : Seq[java.lang.String] = {
    // check if the value can be interpreted as string
    val result = ArrayBuffer[java.lang.String]()

    val rangeTypes = Array[java.lang.String]("string", "float", "age", "level", "boolean", "byte", "ubyte")
    val decodedValue = types("string").asInstanceOf[chitchat.types.Range].decode(value)
    if (decodedValue.isDefined) result += "string"

    result.toList
  }
}
