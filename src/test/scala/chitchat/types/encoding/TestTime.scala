package chitchat.types.encoding

import org.scalatest._

class TestTime extends FunSuite {

  test ("time test") {
    val time = new Time
    assert(time.name == "time")
    assert(time.size == 11)
    assert(time.sizes.mkString(":") == "5:6")
    assert(time.ranges.mkString(":") == "(0,24):(0,59)")
    assert(time.signs.mkString(":") == "false:false")

    var en = time.encode(List[Int](11, 59)).get
    // PPPPP111011:01011
    // ******** => (7, hour)
    //         *** ***** => (107, minute)
    assert(en.mkString(":") == "7:107")
    assert(time.decode(en).get == List(11, 59))

    intercept[java.lang.RuntimeException] {
      en = time.encode(List[Int](-1, 12)).get
    }
    intercept[java.lang.RuntimeException] {
      en = time.encode(List[Int](10, 60)).get
    }
  }
}
