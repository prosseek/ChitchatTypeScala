package chitchat.value

import org.scalatest.FunSuite
import util.types._

class TestValue extends FunSuite {
  test ("simple") {
    // this is the map from name to instances
    val typeInference = TypeInference()
    val instance = typeInference.getChitchatTypeFromLabel("string")
    val v = Value(value = "Hello", label="string", typeInference = typeInference)
    println(v.value)
    println(v.encode.mkString(":"))
  }
}
