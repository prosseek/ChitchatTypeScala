package util.types

class TypeInference(val typesDirectory : String = "") {

  val types = Util.getTypeInstances(typesDirectory)

  def getType(label:String, value:Array[Byte] = null) = {
    if (value != null) {
      getTypeFromValue(value)
    }
  }

  private def getTypeFromValue(value:Array[Byte]) = {
    // check if the value can be interpreted as string
    //if (types("string"))
    "string"
  }
}
