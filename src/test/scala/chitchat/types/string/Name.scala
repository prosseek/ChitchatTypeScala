package chitchat.types.string

import org.scalatest.FunSuite

class TestName extends FunSuite {

  test ("simple") {
    val name = new Name
    assert(!name.check("Hello, my name is John"))
  }

  test ("decode") {
    val name = new Name
    val v = Array[Byte](2, 72, 73)
    assert(name.decode(v).get == "HI")
  }
}