package chitchat.types.range

import org.scalatest._

// class Age extends Bit(name = "age", size = 7, min = 0, max = 127, signed = false)
class TestAge extends FunSuite {
  test ("age") {
    val age = new Age
    assert(age.name == "age")
    assert(age.size == 7)
    assert(age.min == 0)
    assert(age.max == 127)
    assert(age.signed == false)

    assert(age.check(10))
    assert(age.check(0))
    assert(age.check(3))

    assert(!age.check(-129))
    assert(!age.check(128))

    assert(age.decode(age.encode(15).get).get == 15)
    assert(age.decode(age.encode(51).get).get == 51)
  }
}
