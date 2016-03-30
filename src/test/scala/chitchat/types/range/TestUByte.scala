package chitchat.types.range

import org.scalatest._

// class UByte extends Bit(name = "ubyte", size = 8, min = 0, max = 255, signed = false)
class TestUByte extends FunSuite {
  test ("ubyte") {
    val ubyte = new UByte
    assert(ubyte.name == "ubyte")
    assert(ubyte.size == 8)
    assert(ubyte.min == 0)
    assert(ubyte.max == 255)
    assert(ubyte.signed == false)

    assert(ubyte.check(10))
    assert(ubyte.check(0))
    assert(ubyte.check(255))

    assert(!ubyte.check(-1))
    assert(!ubyte.check(257))
  }
}
