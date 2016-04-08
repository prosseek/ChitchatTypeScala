package chitchat.types

import org.scalatest._
import java.lang.{Float => JFloat}

class RangeFloat extends Float("float_rfloat", Seq[JFloat](0.0f, 2.0f))

class TestFloat extends FunSuite {

  def inRange(value1:JFloat, value2:JFloat, tor:JFloat) = {
    (Math.abs(value1 - value2) < tor)
  }

  test ("float test") {
    val f = new Float
    assert(f.name == "float")
    assert(f.size == 4 * 8)
    assert(f.sizeInBytes == 4)

    assert("64:14:-7:-37" == f.encode(1.234f).get.mkString(":"))
    assert(inRange(f.decode(f.encode(1.234f).get).get, 1.234f, 0.0001f))

    // checkRange
    var error = Array[Byte](21, 33, 54, 65, 66)
    assert(f.decode(error).isEmpty)

    // error
    error = Array[Byte](-1, -1, -1, -1)
    assert(f.decode(error).isEmpty)
  }

  test ("float range") {
    val rf = new RangeFloat
    assert(false == rf.check(2.5f))
    assert(true == rf.check(1.0f))

    assert(inRange(rf.decode(rf.encode(1.6f).get).get,1.6f, 0.0001f))
  }

}
