package chitchat.conversion

import org.scalatest.FunSuite
import util.types.TypeInference

class TestEncoder extends FunSuite {
  test ("") {
    val USER_PROVIDED_TYPE_DIRECTORY = ""
    val typeInstances: Map[String, chitchat.types.Base[_]] = util.types.Util.getTypeInstances(USER_PROVIDED_TYPE_DIRECTORY)
    val typeInference = new util.types.TypeInference(typeInstances)

    val encoder = new Encoder(typeInference)

    assert(encoder.encode("time", List(12,11)).toList == List[scala.Byte](1,108))
  }
}
