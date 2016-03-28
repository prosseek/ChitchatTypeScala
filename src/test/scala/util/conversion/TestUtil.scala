package util.conversion

import org.scalatest.FunSuite

class TestUtil extends FunSuite {
/******************************************************************
  * dms to dd
  ******************************************************************/

  test ("dms to dd") {
    // Check for two different argument types: 4 parameters & 1 four-touples
    // Use this site for verification http://www.rapidtables.com/convert/number/degrees-minutes-seconds-to-degrees.htm
    assert(Math.abs(Util.dms2dd(1,2,3,4) - 1.034278) < 0.00001)
    assert(Math.abs(Util.dms2dd((1,2,3,4)) - 1.03428) < 0.00001)
  }

/******************************************************************
  * bit width functions
  ******************************************************************/

  test ("get Bits To Represent The Value (getBitsForValue)") {
    // 0 -> 1, 1 -> 1, 2 -> 1bit, 3 -> 2, 4 -> 2, 5-> 3, 6->3, 7->3, 8->3, 9->4, 10->4
    val results = Array(1, 1,1, 2,2, 3,3,3,3, 4,4)
    (0 to 10).foreach { i =>
      assert(Util.getBitsForValue(i) == results(i))
    }
  }

  test ("getBytesForBits") {
    assert(Util.getBytesForBits(0) == 1)
    assert(Util.getBytesForBits(8) == 1)
    assert(Util.getBytesForBits(9) == 2)
  }
}
