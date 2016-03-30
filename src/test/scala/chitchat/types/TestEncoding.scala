package chitchat.types

import org.scalatest._

/**
  * Created by smcho on 3/28/16.
  */
class TestEncoding extends FunSuite {
  test ("encoding test") {
    val e = new Encoding(name = "Hello", Array[Bit](new Bit(size=100), new Bit(size=200)))
    assert(e.name == "Hello")
    assert(e.size == 300)

    println(e.decode(Array[scala.Byte]()))

  }
}
