package chitchat.types

import org.scalatest._

/**
  * Created by smcho on 3/28/16.
  */
class TestEncoding extends FunSuite {
  test ("encoding test") {
    val e = new Encoding(name = "Hello", Array[Bit](new Bit(name="a", size=5, signed = false, min = 0, max = 10),
                                                    new Bit(name="b", size=5, signed = true, min = -10, max = 10)))
    assert(e.name == "Hello")
    assert(e.size == 10)

    // 5 => 00101 (5bit)
    // -10 => 10110 (5 bit signed)
    // 10110:00101 (big endian)
    assert("2:-59" == e.encode(Seq[Int](5, -10)).mkString(":"))
  }

  test ("decoding test") {
    val e = new Encoding(name = "Hello", Array[Bit](new Bit(name="a", size=5, signed = false, min = 0, max = 10),
                                                    new Bit(name="b", size=5, signed = true, min = -10, max = 10)))

    val byteArray = Array[Byte](2, -59)
    assert(e.decode(byteArray).get.toList == List(5, -10))
  }

}
