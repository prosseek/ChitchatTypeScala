package chitchat.typefactory

import org.scalatest.FunSuite
import java.lang.{String => JString}
import chitchat.types._

class TestTypeDatabase extends FunSuite {
  test ("simple") {
    val directory = "./src/test/resources/util/file/"
    val types = FromClass.getTypeInstances(directory)
    val typeDatabase = TypeDatabase(Seq[JString](directory))

    assert(typeDatabase.get("string").get.name == "string")
    assert(typeDatabase.get("why?").isEmpty)

    val str = typeDatabase.get("string").get.asInstanceOf[String]
    assert(str.encode("Hello").get.mkString(":") == "5:72:101:108:108:111")
    assert(str.decode(str.encode("Hello").get).get == "Hello")
  }
}
