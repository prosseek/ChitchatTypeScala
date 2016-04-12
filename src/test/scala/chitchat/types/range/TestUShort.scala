package chitchat.types.range

import org.scalatest._

// class Byte extends Bit(name = "byte", size = 8, min = -127, max = 128, signed = true)
class TestUShort extends FunSuite {
  test ("ushort") {
    val ushort = new UShort
    assert(ushort.name == "ushort")
    assert(ushort.size == 16)
    assert(ushort.min == 0)
    assert(ushort.max == 65536)
    assert(ushort.signed == false)

    assert(ushort.check(10333))
    assert(!ushort.check(-10))
    assert(!ushort.check(-127))
    assert(ushort.check(12328))

    assert(ushort.check(44444))
    assert(!ushort.check(-44444))
  }
}
