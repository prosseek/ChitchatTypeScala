package chitchat.typetool

import chitchat.typefactory.{FromClass, TypeDatabase}
import java.lang.{String => JString}
import org.scalatest._

class TestTypeInference extends FunSuite with BeforeAndAfterEach with BeforeAndAfterAll {

  var typeInference : TypeInference = null
  override def beforeAll(): Unit = {
    val directory = "./src/test/resources/util/file/"
    val typeDatabase = TypeDatabase(Seq[JString](directory))
    typeInference  = new TypeInference(typeDatabase)
  }

  test ("simple test") {
    val label = "time"
    assert(typeInference.get(label).get.name == label)
    println(typeInference.encode("time", Seq[Int](1,12)))
    //assert("time" == typeInference.getChitchatTypeFromLabel(label).get.name)
    //assert("encoding" == typeInference.getChitchatTypeFromLabel(label).get)
  }

  test ("encodeFromLabel test") {
    val label = "time"
    //val result = typeInference.encodeFromLabel(label, Seq[Int](11, 12)).get
    //assert(result.mkString(":") == "1:-117")
  }

  test ("getTypeFromValue test") {
    val value = Array[Byte](5, 72, 101, 108, 108, 111)
//    assert("string" == typeInference.getTypeFromByteArrayValue(value).mkString(":"))
  }
}