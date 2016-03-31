package util.conversion

import scala.collection.BitSet

object BitSetTool {

  /** Returns BitSet from byte (8 bit)
    *
    * ==== Idea ====
    * We only need byteToBitSet as multi-byte conversion can be implemented with
    * byteArray -> BitSet
    *
    * ==== Why filterBitwidth ====
    *  [[util.conversion.ByteArrayTool.stitch]] requires not selecting higher bits.
    *
    * @param value
    * @param shift
    * @return
    */
  def byteToBitSet(value:Byte, filterBitwidth:Int = 8, shift:Int = 0) = {
    // http://stackoverflow.com/questions/21568800/type-mismatch-for-generic-integral-in-scala
    val op = (x:Byte, s:Int) => ((x >> s) & 1) == 1
    BitSet((for (i <- 0 to (filterBitwidth - 1) if op(value, i) == true) yield (i + shift)): _*)
  }

  /** Returns a value that bitSet represents
    *
    * ==== Example ====
    *
    * Given bitset & bitWidth, it returns the value in integer type.
    * {{{
    *  With Bitset of {0,1,2} in 3 bit => -1 (in 3 bit system 111 is -1)
    * }}}

    * @param b
    * @param bitWidth
    * @param sh
    * @return
    */
  def bitSetToValue(b:BitSet, bitWidth:Int, sh:Int=0) : Int = {
    val res = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))})
    val maxInt = scala.math.pow(2.0, bitWidth-1).toInt - 1
    if (res > maxInt) {
      val mask = (scala.math.pow(2.0, bitWidth).toInt)
      res - mask
    }
    else
      res
  }

}
