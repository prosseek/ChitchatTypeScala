package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class TestPstring extends FunSuite
{
  test ("simple") {
    val pstring = new Pstring
    println(pstring.decode(Array[Byte]()))
  }
}