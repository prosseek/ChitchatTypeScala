package chitchat.types.encoding

import org.scalatest._

class TestLongitude extends FunSuite {

  // (9, -180, 180), (6, 0, 59), (6, 0, 59), (7, 0, 99)
  test("longitude test") {
    val longitude = new Longitude
    assert(longitude.name == "longitude")
    assert(longitude.size == 9 + 6 + 6 + 7)

    assert(longitude.check(List(-180,0,0,0)))
    assert(longitude.check(List(32,11,11,2)))
    assert(longitude.check(List(180,59,59,99)))
    assert(!longitude.check(List(-191,0,0,0)))
    assert(!longitude.check(List(-90,66,0,0)))
    assert(!longitude.check(List(-90,0,66,0)))
    assert(!longitude.check(List(-90,0,0,101)))

  }
}