package chitchat.types

import org.scalatest._
import java.lang.{Float => JFloat}

class TestFloat extends FunSuite {

  def inRange(value1:JFloat, value2:JFloat, tor:JFloat) = {
    (Math.abs(value1 - value2) < tor)
  }

  test ("float test") {
    val f = new Float
    assert(f.name == "float")
    assert(f.size == 4 * 8)
    assert(f.sizeInBytes == 4)

    assert("63:-99:-13:-74" == f.encode(1.234f).get.mkString(":"))
    assert(inRange(f.decode(f.encode(1.234f).get).get, 1.234f, 0.0001f))

    // checkRange
    var error = Array[Byte](21, 33, 54, 65, 66)
    assert(f.decode(error).isEmpty)

    // error
    error = Array[Byte](-1, -1, -1, -1)
    assert(f.decode(error).isEmpty)
  }

  test ("float range test") {
    class RangeFloat extends Float("float_rfloat", min = 0.0f, max = 2.0f)

    val rf = new RangeFloat
    assert(false == rf.check(2.5f))
    assert(true == rf.check(1.0f))

    assert(inRange(rf.decode(rf.encode(1.6f).get).get,1.6f, 0.0001f))
  }

  test ("float range & shift test") {
    class RangeFloat0 extends Float("float_rfloat", min = -100.0f, max = 100.0f)
    class RangeFloat10 extends Float("float_rfloat", min = -100.0f, max = 100.0f, shift = 10.0f)

    val r0 = new RangeFloat0
    val r10 = new RangeFloat10

    val encoded0 = r0.encode(0.0f).get
    assert(encoded0.mkString(":") == "0:0:0:0")
    val encoded10 = r10.encode(0.0f).get
    assert(encoded10.mkString(":") == "-63:32:0:0")

    // it should cause an error with shift value of 10
    assert(r10.decode(encoded0).isEmpty)
    assert(r10.decode(encoded10) == Some(0.0f))


    //assert(inRange(rf.decode(rf.encode(1.6f).get).get,1.6f, 0.0001f))
  }

}
