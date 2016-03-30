package chitchat.types

import org.scalatest.FunSuite

class TestBit extends FunSuite
{
  test ("encode test") {
    // 10 bits (2 byte), but the bytearray size should be 1 byte
    val bit = new Bit(name = "bit", size = 10, min = 0, max = 4)
    assert("3:0" == bit.encode(value = 3, bigEndian = false).mkString(":"))
    assert("0:3" == bit.encode(value = 3, bigEndian = true).mkString(":"))

    val bit2 = new Bit(name = "bit", size = 17, signed=true, min = -10, max = 10)
    assert("-1:-1:-3" == bit2.encode(value = -3, bigEndian = true).mkString(":"))
    assert("-1:-1:-2" == bit2.encode(value = -2, bigEndian = true).mkString(":"))

    assert("-3:-1:-1" == bit2.encode(value = -3, bigEndian = false).mkString(":"))
    assert("-2:-1:-1" == bit2.encode(value = -2, bigEndian = false).mkString(":"))
  }

  test ("decode test") {
    val bit = new Bit(name = "bit", size = 10, min = -20, max = 20, signed = true)
    var byteArray = Array[scala.Byte](0, 2)
    var res = bit.decode(byteArray = byteArray)
    assert(res.get == 2)

    byteArray = Array[scala.Byte](0, 0, 11)
    res = bit.decode(byteArray = byteArray)
    assert(res.get == 11)

    byteArray = Array[scala.Byte](0, 0, 0, 0)
    res = bit.decode(byteArray = byteArray)
    assert(res.get == 0)

    // Error check

    byteArray = Array[scala.Byte](0, 40) // out of range, so None should be in the return value from decode
    res = bit.decode(byteArray = byteArray)
    assert(res.isEmpty)

    byteArray = Array[scala.Byte](-1, -40) // 2 byte negative value & error
    res = bit.decode(byteArray = byteArray)
    assert(res.isEmpty)

    // 2/3/4 byte negative values

    byteArray = Array[scala.Byte](-1, -2) // 2 byte negative value check
    res = bit.decode(byteArray = byteArray)
    assert(res.get == -2)

    byteArray = Array[scala.Byte](-1, -1, -10) // 3 byte negative value check
    res = bit.decode(byteArray = byteArray)
    assert(res.get == -10)

    byteArray = Array[scala.Byte](-1, -1, -1, -10) // 3 byte negative value check
    res = bit.decode(byteArray = byteArray)
    assert(res.get == -10)
  }

  test ("check test") {
    val bit = new Bit(name = "bit", size = 10, min = -20, max = 20, signed = true)
    assert(bit.check(0))
    assert(bit.check(-20))
    assert(bit.check(20))
    assert(!bit.check(-25))
    assert(!bit.check(25))
  }
}