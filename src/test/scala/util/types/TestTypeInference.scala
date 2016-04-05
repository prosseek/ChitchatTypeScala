package util.types

import org.scalatest._

class TestTypeInference extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll {

  var typeInference : TypeInference = null
  override def beforeAll(): Unit = {
    val directory = "./src/test/resources/util/file/"
    val types = util.types.Util.getTypeInstances(directory)
    typeInference  = new TypeInference(types)
  }

  test ("getChitchatTypeFromLabel test") {
    val label = "time"
    //println(typeInference.getType(label).get.name)
    assert("time" == typeInference.getChitchatTypeFromLabel(label).get._2.name)
    assert("encoding" == typeInference.getChitchatTypeFromLabel(label).get._1)
  }

  test ("encodeFromLabel test") {
    val label = "time"
    val result = typeInference.encodeFromLabel(label, Seq[Int](11, 12)).get
    assert(result.mkString(":") == "1:-117")
  }

  test ("getTypeFromValue test") {
    val value = Array[Byte](5, 72, 101, 108, 108, 111)
//    assert("string" == typeInference.getTypeFromByteArrayValue(value).mkString(":"))
  }
}