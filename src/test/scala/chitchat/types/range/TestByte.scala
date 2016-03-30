package chitchat.types.range

import org.scalatest._

// class Byte extends Bit(name = "byte", size = 8, min = -127, max = 128, signed = true)
class TestByte extends FunSuite {
  test ("byte") {
    val byte = new Byte
    assert(byte.name == "byte")
    assert(byte.size == 8)
    assert(byte.min == -127)
    assert(byte.max == 128)
    assert(byte.signed == true)

    assert(byte.check(10))
    assert(byte.check(-10))
    assert(byte.check(-127))
    assert(byte.check(128))

    assert(!byte.check(-129))
    assert(!byte.check(257))
  }
}
