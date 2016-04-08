package chitchat.value

import util.types.TypeInference

object Value {
  def apply(value:Any, label:String, typeInference:TypeInference) = {
    new Value(value = value, label = label, typeInference = typeInference)
  }
}

class Value(val value:Any, val label:String, val typeInference:TypeInference) {
  def encode = {
    typeInference.encodeFromLabel(label, value).get
  }
}
