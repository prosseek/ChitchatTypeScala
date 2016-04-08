package chitchat.types.encoding

import org.scalatest._

/**
  * Created by smcho on 3/28/16.
  */
class TestDate extends FunSuite {

  test ("date test") {
    val date = new Date
    assert(date.name == "date")
    assert(date.size == 16)
    assert(date.sizes.mkString(":") == "7:4:5")
    assert(date.ranges.mkString(":") == "(-64,63):(1,12):(1,31)")
    assert(date.signs.mkString(":") == "true:false:false")

    var en = date.encode(List[Int](10, 3, 12)).get
    // 01100001:10001010 (97:-118, big endian, read from right to left)
    //           ******* (year = 10)
    //      *** * (month = 3)
    // ***** (day = 12)
    assert(en.mkString(":") == "97:-118")
    assert(date.decode(en).get == List(10,3,12))

    intercept[java.lang.RuntimeException] {
      en = date.encode(List[Int](130, 10, 12)).get
    }
    intercept[java.lang.RuntimeException] {
      en = date.encode(List[Int](10, 10, 43)).get
    }
    intercept[java.lang.RuntimeException] {
      en = date.encode(List[Int](10, 10, -1)).get
    }
  }
}
