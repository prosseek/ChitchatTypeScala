package chitchat.types

import org.scalatest._

/**
  * Created by smcho on 3/28/16.
  */
class TestEncoding extends FunSuite {
  test ("encoding test") {
    val e = new Encoding(name = "Hello", Array[Range](new Range(name="a", size=5, signed = false, min = 0, max = 10),
                                                    new Range(name="b", size=5, signed = true, min = -10, max = 10)))
    assert(e.name == "Hello")
    assert(e.size == 10)

    // 5 => 00101 (5bit)
    // -10 => 10110 (5 bit signed)
    // 10110:00101 (big endian)
    assert("2:-59" == e.encode(Seq[Int](5, -10)).get.mkString(":"))
  }

  test ("decoding test") {
    val e = new Encoding(name = "Hello", Array[Range](new Range(name="a", size=5, signed = false, min = 0, max = 10),
                                                    new Range(name="b", size=5, signed = true, min = -10, max = 10)))
    var byteArray = Array[Byte](2, -59)
    assert(e.decode(byteArray).get.toList == List(5, -10))

    // decode error
    byteArray = Array[Byte](2, -59, 3)
    assert(e.decode(byteArray).isEmpty)
  }

  test ("decoding with added bytes") {
    val e = new Encoding(
      name = "time",
      Array[Range](
        new Range(name = "hour",     size = 5, min = 0, max = 24, signed = false),
        new Range(name = "minute",   size = 6, min = 0, max = 59, signed = false)))

    var byteArray = Array[Byte](1, 108, 0, 0, 0, 0, 0, 0)
    assert(e.decode(byteArray).get.toList == List(12, 11))
  }

  test ("wrong decoding error") {
    val e = new Encoding(
      name = "time",
      Array[Range](
        new Range(name = "hour",     size = 5, min = 0, max = 24, signed = false),
        new Range(name = "minute",   size = 6, min = 0, max = 59, signed = false)))

    var byteArray = Array[Byte](-1, -1, 0, 0, 0, 0, 0, 0)
    assert(e.decode(byteArray).isEmpty)
  }
}
