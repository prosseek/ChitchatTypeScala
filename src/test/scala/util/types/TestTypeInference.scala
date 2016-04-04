package util.types

import org.scalatest._

class TestTypeInference extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll {

  var typeInference : TypeInference = null
  override def beforeAll(): Unit = {
    val directory = "./src/test/resources/util/file/"
    val types = util.types.Util.getTypeInstances(directory)
    typeInference  = new TypeInference(types)
  }

  test ("getType") {
    val label = "time"
    assert("time" == typeInference.getType(label).get.name)
  }

  test ("getTime type and setup") {
    val encodingTypes = Set("time")

    val label = "time"
    if (encodingTypes.contains(label)) {
      val t = typeInference.getType(label).get
      val res = t.asInstanceOf[chitchat.types.Encoding].encode(Seq[Int](12,14))
      println(res.mkString(":"))
      println(t.asInstanceOf[chitchat.types.Encoding].decode(res))
    }
  }

  test ("getTypeFromValue") {
    val value = Array[Byte](5, 72, 101, 108, 108, 111)
    assert("string" == typeInference.getTypeFromByteArrayValue(value).mkString(":"))
  }
}