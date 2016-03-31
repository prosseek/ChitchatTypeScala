package util.conversion

import org.scalatest._
import scala.collection.BitSet

class TestByteArrayTool extends FunSuite {
  // adjust
  test ("adjust test shrink/big endian") {
    var value = Array[Byte](0, 0, 0, 0, 1) // big endian
    assert("0:1" == ByteArrayTool.adjust(value, goalSize = 2, bigEndian = true).mkString(":"))

    value = Array[Byte](-1, -1, -1, -1, -2) // big endian
    assert("-1:-2" == ByteArrayTool.adjust(value, goalSize = 2, bigEndian = true).mkString(":"))
  }

  test ("adjust test shrink/little endian") {
    var value = Array[Byte](1, 0, 0, 0, 0) // big endian
    assert("1:0" == ByteArrayTool.adjust(value, goalSize = 2, bigEndian = false).mkString(":"))

    value = Array[Byte](-2, -1, -1, -1) // big endian
    assert("-2:-1" == ByteArrayTool.adjust(value, goalSize = 2, bigEndian = false).mkString(":"))
  }

  test ("adjust test expand/big endian sign-expanded") {
    var value = Array[Byte](0, 1) // big endian
    assert("0:0:0:0:1" == ByteArrayTool.adjust(value, goalSize = 5, signExtension=true, bigEndian = true).mkString(":"))

    value = Array[Byte](-1, -2) // big endian
    assert("-1:-1:-1:-1:-2" == ByteArrayTool.adjust(value, goalSize = 5, signExtension=true, bigEndian = true).mkString(":"))
  }

  // stitch
  test ("stitch test") {

    val byteArray1 = Array[Byte](1,0,0,-1) // 4 bytes, big-endian, I'll use only 3 bits (ignore 1, and high bits of -1 (0xFF))
    val byteArray2 = Array[Byte](0,0,7) // 3 bytes, big-endian, only
    val res = ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](3,3))
    println(res)
  }

  // string
  test ("stringToByteArray") {
    var value = "Hello, world"
    assert(ByteArrayTool.stringToByteArray(value)(0) == value.size)
    assert(new String(ByteArrayTool.stringToByteArray(value).slice(1, 1 + value.size), "ASCII") == value)
  }

  test ("ByteArrayToString") {
    var value = "Hello"
    var ba = Array[Byte](5) ++ value.getBytes
    assert(value == ByteArrayTool.byteArrayToString(ba))

    value = List.fill(255)("a").mkString
    ba = Array[Byte]((255 & 0xFF).toByte) ++ value.getBytes
    assert(value == ByteArrayTool.byteArrayToString(ba))
  }

  // byte
  test ("byte to byte array and back test ") {
    var value : Byte = 0
    assert(value == ByteArrayTool.byteArrayToByte(ByteArrayTool.byteToByteArray(value)))
    value = Byte.MaxValue
    assert(value == ByteArrayTool.byteArrayToByte(ByteArrayTool.byteToByteArray(value)))
    value = Byte.MinValue
    assert(value == ByteArrayTool.byteArrayToByte(ByteArrayTool.byteToByteArray(value)))
    value = 123
    assert(value == ByteArrayTool.byteArrayToByte(ByteArrayTool.byteToByteArray(value)))
    value = -123
    assert(value == ByteArrayTool.byteArrayToByte(ByteArrayTool.byteToByteArray(value)))
  }

  // short
  test ("short to byte array and back test ") {
    var value = 0.toShort
    assert(value == ByteArrayTool.byteArrayToShort(ByteArrayTool.shortToByteArray(value)))
    value = Short.MaxValue
    assert(value == ByteArrayTool.byteArrayToShort(ByteArrayTool.shortToByteArray(value)))
    value = Short.MinValue
    assert(value == ByteArrayTool.byteArrayToShort(ByteArrayTool.shortToByteArray(value)))
    value = (-2).toShort
    val s = ByteArrayTool.shortToByteArray(value)
    val a = ByteArrayTool.byteArrayToShort(s)
    assert(value == a)
  }

  test ("short endian check") {
    var value = 1.toShort
    val expected = Array[Byte](1, 0)
    assert(expected.sameElements(ByteArrayTool.shortToByteArray(value, bigEndian = false)))
    assert(value == ByteArrayTool.byteArrayToShort(expected, bigEndian = false))

    value = Short.MinValue
    assert(value == ByteArrayTool.byteArrayToShort(ByteArrayTool.shortToByteArray(value, false), false))
  }

  // int
  test ("int to byte array and back test ") {
    var value = 0
    assert(value == ByteArrayTool.byteArrayToInt(ByteArrayTool.intToByteArray(value)))
    value = Int.MaxValue
    assert(value == ByteArrayTool.byteArrayToInt(ByteArrayTool.intToByteArray(value)))
    value = Int.MinValue
    assert(value == ByteArrayTool.byteArrayToInt(ByteArrayTool.intToByteArray(value)))
    value = 12343434
    assert(value == ByteArrayTool.byteArrayToInt(ByteArrayTool.intToByteArray(value)))
  }

  test ("when byte array size is more than 4 bytes") {
    var intValue = 1
    //assert(ByteArrayTool.intToByteArray(intValue, 10).mkString(":") == "0:0:0:1:0:0:0:0:0:0")

    intValue = -1
    //assert(ByteArrayTool.intToByteArray(intValue, 10).mkString(":") == "-1:-1:-1:-1:0:0:0:0:0:0")

    var value = Array[Byte](0,0,0,1,0)
    assert(ByteArrayTool.byteArrayToInt(value) == 1)
  }

  test ("int endian check") {
    var value = 1.toInt
    val expected = Array[Byte](1, 0, 0, 0)
    assert(expected.sameElements(ByteArrayTool.intToByteArray(value, bigEndian = false)))
    assert(value == ByteArrayTool.byteArrayToInt(expected, bigEndian = false))

    value = Int.MinValue
    assert(value == ByteArrayTool.byteArrayToInt(ByteArrayTool.intToByteArray(value, false), false))
  }

  test("when byte array size is less than 4 bytes") {
    intercept[java.lang.RuntimeException] {
      var value = Array[Byte](0, 0, 1)
      ByteArrayTool.byteArrayToInt(value)
    }
  }

  // long
  test ("long to byte array and back test ") {
    var value = 0L
    assert(value == ByteArrayTool.byteArrayToLong(ByteArrayTool.longToByteArray(value)))
    value = Long.MaxValue
    assert(value == ByteArrayTool.byteArrayToLong(ByteArrayTool.longToByteArray(value)))
    value = Long.MinValue
    assert(value == ByteArrayTool.byteArrayToLong(ByteArrayTool.longToByteArray(value)))
    value = 12343434L
    assert(value == ByteArrayTool.byteArrayToLong(ByteArrayTool.longToByteArray(value)))
  }

  test ("long endian check") {
    var value = 1.toLong
    val expected = Array[Byte](1, 0, 0, 0, 0, 0, 0, 0)
    assert(expected.sameElements(ByteArrayTool.longToByteArray(value, bigEndian = false)))
    assert(value == ByteArrayTool.byteArrayToLong(expected, bigEndian = false))

    value = Long.MinValue
    assert(value == ByteArrayTool.byteArrayToLong(ByteArrayTool.longToByteArray(value, false), false))
  }

  test("when byte array size is less than 8 bytes") {
    intercept[java.lang.RuntimeException] {
      var value = Array[Byte](0, 0, 1)
      ByteArrayTool.byteArrayToLong(value)
    }
  }

  // bitset

  test("bitSet to byte array test") {
    // LOW --- HIGH
    // 1111 0000 1010 ...
    // 15        5    ...
    var x = BitSet(0,1,2,3,8,10,104)
    var y1 = ByteArrayTool.bitSetToByteArray(x, bigEndian = true)
    assert(y1.mkString(":") == "1:0:0:0:0:0:0:0:0:0:0:0:5:15")
    var y2 = ByteArrayTool.bitSetToByteArray(x, bigEndian = false)
    assert(y2.mkString(":") == "15:5:0:0:0:0:0:0:0:0:0:0:0:1")

    assert(ByteArrayTool.byteArrayToBitSet(y1, bigEndian = true) == x)
    assert(ByteArrayTool.byteArrayToBitSet(y2, bigEndian = false) == x)

    x = BitSet(0,1,2,3,4,5,6,7,8)
    var y = ByteArrayTool.bitSetToByteArray(x, bigEndian = false)
    assert(y.mkString(":") == "-1:1")
    assert(ByteArrayTool.byteArrayToBitSet(y, bigEndian = false) == x)

    x = BitSet(0)
    y = ByteArrayTool.bitSetToByteArray(x, goalSize = 4, bigEndian = false)
    assert(y.mkString(":") == "1:0:0:0") // LOW - HIGH bits
    assert(ByteArrayTool.byteArrayToBitSet(y, bigEndian = false) == x)

    x = BitSet(0)
    y = ByteArrayTool.bitSetToByteArray(x, goalSize = 4, bigEndian = true)
    assert(y.mkString(":") == "0:0:0:1") // LOW - HIGH bits
    assert(ByteArrayTool.byteArrayToBitSet(y, bigEndian = true) == x)

    // special case when there is no 1 in the value
    x = BitSet()
    y = ByteArrayTool.bitSetToByteArray(x)
    assert(y.mkString(":") == "")
    y = ByteArrayTool.bitSetToByteArray(x, 10)
    assert(y.mkString(":") == "0:0:0:0:0:0:0:0:0:0")
  }

  test("byte array to bitset test") {
    val byteArray = Array[Byte](1,0,0,-1) //

    // For big endian, we read <= (right to left direction)
    assert(BitSet(0, 1, 2, 3, 4, 5, 6, 7, 24) == ByteArrayTool.byteArrayToBitSet(byteArray = byteArray, bigEndian = true, shift = 0))

    // for little endian, we read => direction
    // The endianness is byte level, not bit level
    assert(BitSet(0, 24, 25, 26, 27, 28, 29, 30, 31) == ByteArrayTool.byteArrayToBitSet(byteArray = byteArray, bigEndian = false, shift = 0))
  }

  // double
  test ("double to byte array and back test ") {
    var value = 12.34
    assert(value == ByteArrayTool.byteArrayToDouble(ByteArrayTool.doubleToByteArray(value)))
    value = 1234.56
    assert(value == ByteArrayTool.byteArrayToDouble(ByteArrayTool.doubleToByteArray(value)))
    value = 0.001234
    assert(value == ByteArrayTool.byteArrayToDouble(ByteArrayTool.doubleToByteArray(value)))
    value = 0.0
    assert(value == ByteArrayTool.byteArrayToDouble(ByteArrayTool.doubleToByteArray(value)))
  }

  // float
  test ("float to byte array and back test ") {
    var value = 12.34F
    assert(value == ByteArrayTool.byteArrayToFloat(ByteArrayTool.floatToByteArray(value)))
    value = 1234.56F
    assert(value == ByteArrayTool.byteArrayToFloat(ByteArrayTool.floatToByteArray(value)))
    value = 0.001234F
    assert(value == ByteArrayTool.byteArrayToFloat(ByteArrayTool.floatToByteArray(value)))
    value = 0.0F
    assert(value == ByteArrayTool.byteArrayToFloat(ByteArrayTool.floatToByteArray(value)))
  }
}
