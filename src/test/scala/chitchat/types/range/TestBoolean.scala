package chitchat.types.range

import org.scalatest._

// class Boolean extends Bit(name = "boolean", size = 8, min = 0, max = 1)
class TestBoolean extends FunSuite {
  test ("byte") {
    val boolean = new Boolean
    assert(boolean.name == "boolean")
    assert(boolean.size == 8)
    assert(boolean.min == 0)
    assert(boolean.max == 1)
    assert(boolean.signed == false)

    assert(boolean.check(1))
    assert(boolean.check(0))

    assert(!boolean.check(-129))
    assert(!boolean.check(257))
  }
}
