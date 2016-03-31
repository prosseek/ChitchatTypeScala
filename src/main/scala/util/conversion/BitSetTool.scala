package util.conversion

import scala.collection.BitSet

object BitSetTool {

  /** Returns BitSet from byte (8 bit)
    *
    * ==== Idea ====
    *
    * When we identify the bit position N, we can shift (>>) the bit position by N to apply the AND operation.
    * If the operation result is 1, we add the N in the BitSet.
    *
    * The bit position is different depending on the endianness.
    *
    * {{{
    * LittleEndian:
    *
    *   LSB - MSB
    *   10001000
    *   --------
    *   01234567 <-- bit position
    *   {0, 4}   <-- BitSet
    *
    * BigEndian:
    *
    *   MSB - LSB
    *   10001000
    *   --------
    *   76543210  <-- bit position
    *   {7, 3}    <-- BitSet
    *
    * }}}
    *
    * We use >> operator for getting the bit at the k position, so bigEndian is the default endian.
    *
    * ==== Algorithm ====
    *  1. Iterate over 0 to (filterBitwidth - 1)
    *  2. Use >> i for shifting
    *      1. Big endian, LSB is at the rightmost, so just shift i
    *      2. Little endina, LSB is at the left most, so shift (valueBitwidht - i)
    *
    * ==== Why filterBitwidth ====
    *  [[util.conversion.ByteArrayTool.stitch]] requires not selecting higher bits.
    *
    *
    * @param value
    * @param bigEndian
    * @param shift
    * @return
    */
  // * TODO: The code can be shorter with generics, but I can't find functions to replace the operators >> and &.
  //         Refer to [[http://stackoverflow.com/questions/36320345/type-parameter-issue-in-scala-with-generic-function/]]
  def byteToBitSet(value:Byte, filterBitwidth:Int = 8, bigEndian:Boolean = true, shift:Int = 0) = {
    val valueBitwidth:Int = 8
    // http://stackoverflow.com/questions/21568800/type-mismatch-for-generic-integral-in-scala
    val op = if (bigEndian) ((x:Byte, s:Int) => ((x >> s) & 1) == 1)
                       else ((x:Byte, s:Int) => ((x >> (valueBitwidth - s - 1) & 1) == 1))

    BitSet((for (i <- 0 to (filterBitwidth - 1) if op(value, i) == true) yield (i + shift)): _*)
  }

  def shortToBitSet(value:Short, filterBitwidth:Int = 8*2, bigEndian:Boolean = true, shift:Int = 0) = {
    val valueBitwidth:Int = 8*2
    // http://stackoverflow.com/questions/21568800/type-mismatch-for-generic-integral-in-scala
    val op = if (bigEndian) ((x:Short, s:Int) => ((x >> s) & 1) == 1)
                       else ((x:Short, s:Int) => ((x >> (valueBitwidth - s - 1) & 1) == 1))

    BitSet((for (i <- 0 to (filterBitwidth - 1) if op(value, i) == true) yield (i + shift)): _*)
  }

  def intToBitSet(value:Int, filterBitwidth:Int = 8*4, bigEndian:Boolean = true, shift:Int = 0) = {
    val valueBitwidth:Int = 8*4
    // http://stackoverflow.com/questions/21568800/type-mismatch-for-generic-integral-in-scala
    val op = if (bigEndian) ((x:Int, s:Int) => ((x >> s) & 1) == 1)
                       else ((x:Int, s:Int) => ((x >> (valueBitwidth - s - 1) & 1) == 1))

    BitSet((for (i <- 0 to (filterBitwidth - 1) if op(value, i) == true) yield (i + shift)): _*)
  }

  /** Returns byte value from bitSet
    *
    * @param b
    * @param sh
    * @return
    */
  def bitSetToByte(b:BitSet, sh:Int=0) : Byte = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))}).toByte
  def bitSetToShort(b:BitSet, sh:Int=0) : Short = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))}).toShort
  def bitSetToInt(b:BitSet, sh:Int=0) : Int = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))})

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
