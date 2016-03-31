package util.conversion

import scala.collection.BitSet

object BitSetTool {

  /** Returns BitSet from byte (8 bit)
    *
    * ==== Idea ====
    * We only need byteToBitSet as multi-byte conversion can be implemented with
    * byteArray -> BitSet
    *
    * @param value
    * @param shift
    * @return
    */
  def byteToBitSet(value:Byte, shift:Int = 0) = {
    // http://stackoverflow.com/questions/21568800/type-mismatch-for-generic-integral-in-scala
    val op = (x:Byte, s:Int) => ((x >> s) & 1) == 1
    BitSet((for (i <- 0 to (8 - 1) if op(value, i) == true) yield (i + shift)): _*)
  }

  /** Returns a value that bitSet represents
    *
    * ==== Example ====
    *
    * Given bitset & bitWidth, it returns the value in integer type.
    * {{{
    *  With Bitset of {0,1,2} in 3 bit => -1 (in 3 bit system 111 is -1)
    * }}}
 *
 * @param bitset
    * @param bitWidth
    * @param sh
    * @return
    */
  def bitSetToValue(bitset:BitSet, bitWidth:Int, signed:Boolean=true, sh:Int=0) : Int = {
    val res = ((0 /: bitset) { (acc, input) => acc + (1 << (input - sh))})
    val maxInt = scala.math.pow(2.0, bitWidth-1).toInt - 1

    if (signed) {
      if (res > maxInt) {
        val mask = (scala.math.pow(2.0, bitWidth).toInt)
        res - mask
      }
      else
        res
    }
    else // if not signed
      res
  }

}
