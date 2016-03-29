package util.types

class TypeInference {

  def getType(label:String, value:Array[Byte] = null) = {
    if (value != null) {
      getTypeFromValue(value)
    }
  }

  private def getTypeFromValue(value:Array[Byte]) = {
    "string"
  }
}