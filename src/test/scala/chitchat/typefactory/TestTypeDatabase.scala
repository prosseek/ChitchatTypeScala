package chitchat.typefactory

import org.scalatest.FunSuite
import java.lang.{String => JString}
import chitchat.types._

class TestTypeDatabase extends FunSuite {
  test ("simple - string/float") {
    val directory = "./src/test/resources/util/file/"
    val types = FromClass.getTypeInstances(directory)
    val typeDatabase = TypeDatabase(Seq[JString](directory))

    assert(typeDatabase.get("string").get.name == "string")
    assert(typeDatabase.get("why?").isEmpty)

    val str = typeDatabase.get("string").get.asInstanceOf[String]
    assert(str.encode("Hello").get.mkString(":") == "5:72:101:108:108:111")
    assert(str.decode(str.encode("Hello").get).get == "Hello")

    val flt = typeDatabase.get("float").get.asInstanceOf[Float]
    val fltEncode = flt.encode(1.23f).get
    assert(fltEncode.mkString(":") == "64:14:-72:82")
    assert(Math.abs(flt.decode(fltEncode).get - 1.23) < 0.001)
  }

  test ("encode/decode test - encoding") {
    val directory = "./src/test/resources/util/file/"
    val types = FromClass.getTypeInstances(directory)
    val typeDatabase = TypeDatabase(Seq[JString](directory))

    val tm = typeDatabase.encode("time", Seq[Int](11, 12)).get
    assert(tm.mkString(":") == "1:-117")
    assert(typeDatabase.decode("time", tm).get == List(11,12))
  }

  test ("encode/decode test - range") {
    val directory = "./src/test/resources/util/file/"
    val types = FromClass.getTypeInstances(directory)
    val typeDatabase = TypeDatabase(Seq[JString](directory))

    val tm = typeDatabase.encode("age", 11).get
    assert(tm.mkString(":") == "11")
    assert(typeDatabase.decode("age", tm).get == 11)
  }
}
