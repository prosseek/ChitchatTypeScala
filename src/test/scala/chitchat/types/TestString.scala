package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class TestString extends FunSuite
{
  test ("simple") {
    val pstring = new String
    assert("5:72:101:108:108:111" == pstring.encode("Hello").mkString(":"))

    assert(pstring.decode(pstring.encode("Hello")).get == "Hello")
  }

  test ("Wrong string") {
    val pstring = new String
    val str = Array[Byte](5, 71, 0, 0, 1, 1, 0)
    assert(pstring.decode(str).isEmpty)
  }
}