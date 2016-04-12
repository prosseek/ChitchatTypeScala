package chitchat.types.range

import org.scalatest._

// class Byte extends Bit(name = "byte", size = 8, min = -127, max = 128, signed = true)
class TestShort extends FunSuite {
  test ("short") {
    val short = new Short
    assert(short.name == "short")
    assert(short.size == 16)
    assert(short.min == -32768)
    assert(short.max == 32767)
    assert(short.signed == true)

    assert(short.check(10))
    assert(short.check(-10))
    assert(short.check(-127))
    assert(short.check(128))

    assert(!short.check(44444))
    assert(!short.check(-44444))
  }
}
