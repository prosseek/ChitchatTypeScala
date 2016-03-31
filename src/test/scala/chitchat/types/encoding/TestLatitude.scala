package chitchat.types.encoding

import org.scalatest._

class TestLatitude extends FunSuite {

  // (8, -90, 90), (6, 0, 59), (6, 0, 59), (7, 0, 99)
  test("latitude test") {
    val latitude = new Latitude
    assert(latitude.name == "latitude")
    assert(latitude.size == 8 + 6 + 6 + 7)

    assert(latitude.check(List(-90,0,0,0)))
    assert(latitude.check(List(32,11,11,2)))
    assert(latitude.check(List(90,59,59,99)))
    assert(!latitude.check(List(-91,0,0,0)))
    assert(!latitude.check(List(-90,66,0,0)))
    assert(!latitude.check(List(-90,0,66,0)))
    assert(!latitude.check(List(-90,0,0,101)))

  }
}