package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class TestString extends FunSuite
{
  test ("simple") {
    val pstring = new String
    println(pstring.decode(Array[scala.Byte]()))
  }
}