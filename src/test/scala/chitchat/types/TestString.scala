package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class FTest extends String (name = "f")

class TestString extends FunSuite
{
  test ("simple") {
    val pstring = new String
    assert("5:72:101:108:108:111" == pstring.encode("Hello").get.mkString(":"))

    assert(pstring.decode(pstring.encode("Hello").get).get == "Hello")
  }

  test ("6 bytes string, 10 size byte array") {
    val pstring = new String
    val byteArray = util.conversion.ByteArrayTool.zeroPatch(pstring.encode("Hello").get, 10)
    val result = pstring.decode(byteArray)
    assert(result.get == "Hello")
  }

  test ("Wrong string") {
    val pstring = new String
    val str = Array[Byte](5, 71, 0, 0, 1, 1, 0)
    assert(pstring.decode(str).isEmpty)
  }

  test ("string extension test") {
    val f = new FTest
    f.name == "string_f"
  }
}