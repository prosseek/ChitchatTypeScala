package util.types

import org.scalatest._

class TestTypeInference extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll {

  var typeInference : TypeInference = null
  override def beforeAll(): Unit = {
    typeInference  = new TypeInference
  }

  test ("getType") {
    val label = "time"
    assert("time" == typeInference.getType(label).get.name)
  }

  test ("getTypeFromValue") {
    val value = Array[Byte](5, 72, 101, 108, 108, 111)
    assert("string" == typeInference.getTypeFromValue(value).mkString(":"))
  }
}