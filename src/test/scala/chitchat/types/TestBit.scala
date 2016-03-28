package chitchat.types

import java.io.File
import org.scalatest.FunSuite

class TestBit extends FunSuite
{
  test ("simple") {
    println(new File(".").getAbsolutePath())
  }
}