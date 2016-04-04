package util.conversion

import org.scalatest._
import scala.collection.BitSet

class TestByteArrayTool extends FunSuite {
  // zeroPatch
  test("zero patch") {
    var value = Array[Byte](1,2,3,4)
    var res = ByteArrayTool.zeroPatch(value, 6)
    assert(res.mkString(":") == "1:2:3:4:0:0")

    intercept[RuntimeException] {
      ByteArrayTool.zeroPatch(value, 3)
    }
  }

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
    var byteArray1 = Array[Byte](1,0,0,-1) // 4 bytes, big-endian, I'll use only 3 bits (ignore 1, and high bits of -1 (0xFF))
    var byteArray2 = Array[Byte](0,0,7) // 3 bytes, big-endian, only
    // BitSet(0, 1, 2, 3, 4, 5)
    assert("63" == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](3,3)).mkString(":"))
    // 2^6 - 1 == 63
    byteArray1 = Array[Byte](1,0,0,-1) // 4 bytes, big-endian, I'll use only 3 bits (ignore 1, and high bits of -1 (0xFF))
    byteArray2 = Array[Byte](0,0,3) // 3 bytes, big-endian, only
    // assert(BitSet(0, 1, 2, 3, 4, 5, 6) == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](5,3)))
    assert("127" == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](5,3)).mkString(":"))

    byteArray1 = Array[Byte](1,0,0,-1) // 4 bytes, big-endian, I'll use only 3 bits (ignore 1, and high bits of -1 (0xFF))
    byteArray2 = Array[Byte](0,0,7) // 3 bytes, big-endian, only
    // assert(BitSet(0, 1, 2, 3, 4, 5, 6, 7, 8) == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](5,3)))
    assert("1:-1" == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](6,3)).mkString(":"))
  }
  // stich for debugging
  test ("stitch for debugging") {
    var byteArray1 = Array[Byte](5)
    var byteArray2 = Array[Byte](-10)
    // bigendian
    // -10:5 (value) => 10110:00101 => 10:11000101 => 2:
    assert("2:-59" == ByteArrayTool.stitch(Seq[Array[Byte]](byteArray1, byteArray2), Seq[Int](5,5)).mkString(":"))
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

    assert(ByteArrayTool.byteArrayToBitSet(y1) == x)

    x = BitSet(0,1,2,3,4,5,6,7,8)
    var y = ByteArrayTool.bitSetToByteArray(x)
    assert(y.mkString(":") == "1:-1")
    assert(ByteArrayTool.byteArrayToBitSet(y) == x)

    x = BitSet(0)
    y = ByteArrayTool.bitSetToByteArray(x, goalSize = 4)
    assert(y.mkString(":") == "0:0:0:1") // LOW - HIGH bits
    assert(ByteArrayTool.byteArrayToBitSet(y) == x)

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
    assert(BitSet(0, 1, 2, 3, 4, 5, 6, 7, 24) == ByteArrayTool.byteArrayToBitSet(byteArray = byteArray, shift = 0))
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
