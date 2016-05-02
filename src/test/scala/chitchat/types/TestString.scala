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

  test ("check test") {
    // only 'a' (97) and 'b' (98) are allowed
    class FTest2 extends String (name = "f", min = 97.toChar, max = 'b')
    val pstring = new FTest2
    val str = Array[Byte](4, 97,97,98,98)
    assert(pstring.decode(str).isDefined)
  }

  test ("check test2") {
    // only 'a' (97) and 'b' (98) are allowed
    class FTest2 extends String (name = "f", min = 'a', max = 'b')
    val pstring = new FTest2
    val str = Array[Byte](4, 91,91,98,98)
    assert(pstring.decode(str).isEmpty)
  }

  test ("check test maxlength ok") {
    class FTest2 extends String (name = "f", conditions = List("maxlength", 10))
    val pstring = new FTest2
    val str = Array[Byte](4, 97,97,98,98)
    assert(pstring.decode(str).isDefined)
  }

  test ("check test maxlength error") {
    class FTest2 extends String (name = "f", conditions = List("maxlength", 2))
    val pstring = new FTest2
    val str = Array[Byte](4, 97,97,98,98)
    assert(pstring.decode(str).isEmpty)
  }

  test ("check test minlength ok") {
    class FTest2 extends String (name = "f", conditions = List("minlength", 1))
    val pstring = new FTest2
    val str = Array[Byte](4, 97,97,98,98)
    assert(pstring.decode(str).isDefined)
  }

  test ("check test minlength error") {
    class FTest2 extends String (name = "f", conditions = List("minlength", 12))
    val pstring = new FTest2
    val str = Array[Byte](4, 97,97,98,98)
    assert(pstring.decode(str).isEmpty)
  }
}