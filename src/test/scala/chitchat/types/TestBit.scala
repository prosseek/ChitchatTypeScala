package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class TestBit extends FunSuite
{
  test ("simple") {
    val bit = new Bit(name = "bit", size = 10, min = 0, max = 4)
    println(bit.decode(Array[Byte]()))
  }



}