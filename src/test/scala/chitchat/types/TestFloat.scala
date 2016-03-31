package chitchat.types

import org.scalatest._

class TestFloat extends FunSuite {
  test ("float test") {
    val f = new Float
    assert(f.name == "float")
    assert(f.size == 4)

    assert("63:-99:-13:-74" == f.encode(1.234f).mkString(":"))
    assert((f.decode(f.encode(1.234f)).get - 0.234f) < 0.0001)
  }
}
