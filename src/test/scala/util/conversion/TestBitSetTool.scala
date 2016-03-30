package util.conversion

import org.scalatest._
import scala.collection.BitSet

/**
 * BitSet uses a set of location to indicate a number.
 * 0 => 1 (as 0th location is set to 1)
 */
class TestBitSetTool extends FunSuite {
  test ("byteToBitSet test") {
    // when all the bits are 1, the value is -1
    assert(BitSetTool.byteToBitSet(-1) == BitSet(Range(0,8):_*))
    // when no bit is set, the value is 0
    assert(BitSetTool.byteToBitSet(0) == BitSet())
    // when the first (0th) bit is set, the value is 1 == 2^0
    assert(BitSetTool.byteToBitSet(1) == BitSet(0))
  }
  test ("byteToBitSet with shift test") {
    assert(BitSetTool.byteToBitSet(-1, 8) == BitSet(8,9,10,11,12,13,14,15))
    assert(BitSetTool.byteToBitSet(0, 8) == BitSet())
    assert(BitSetTool.byteToBitSet(1, 8) == BitSet(8))
    // 1 = 2^0 => 0
    assert(BitSetTool.byteToBitSet(1) == BitSet(0))
  }
  test("bitSetToByte") {
    assert(BitSetTool.bitSetToByte(BitSet(0,1,2,3)) == 15)
    assert(BitSetTool.bitSetToByte(BitSet(0,1,2,3,4,5,6,7)) == -1)
  }

  test ("shortToBitSet test") {
    assert(BitSetTool.shortToBitSet(-1) == BitSet(Range(0,16):_*))
    assert(BitSetTool.shortToBitSet(0) == BitSet())
    assert(BitSetTool.shortToBitSet(1) == BitSet(0))
  }

  test ("bitSetToInt") {
    assert(BitSetTool.bitSetToInt(BitSet(0,1,2)) == 7)
  }
  test ("bitSetToInt when the result is negative") {
    assert(BitSetTool.bitSetToValue(BitSet(0,1,2), bitWidth=3) == -1)
    assert(BitSetTool.bitSetToValue(BitSet(0,1,2,3,4,7,8), bitWidth=9) == -97)
  }
}