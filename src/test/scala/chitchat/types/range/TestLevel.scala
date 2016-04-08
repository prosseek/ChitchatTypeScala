package chitchat.types.range

import org.scalatest._

// class Level extends Bit(name = "level", size = 4, min = 0, max = 10, signed = false)
class TestLevel extends FunSuite {
  test ("level") {
    val level = new Level
    assert(level.name == "level")
    assert(level.size == 4)
    assert(level.min == 0)
    assert(level.max == 10)
    assert(level.signed == false)

    assert(level.check(10))
    assert(level.check(0))
    assert(level.check(3))

    assert(!level.check(-129))
    assert(!level.check(257))

    assert(level.decode(level.encode(5).get).get == 5)
  }
}
