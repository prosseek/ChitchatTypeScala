package util

import scala.collection.BitSet

object BitSetTool {
  /**
    * Byte(8)/Short(16)/Int(32) -> bitSet
    */

  def byteToBitSet(x:Byte, shift:Int = 0) = {
    // _* teaches compiler that we are dealing with a vararg, not a seq
    BitSet((for (i <- 0 to (8 - 1) if (((x & 0xFF) >> i) & 1) == 1) yield (i + shift)): _*)
  }
  def shortToBitSet(x:Short, shift:Int = 0) = {
    BitSet((for (i <- 0 to (16 - 1) if (((x & 0xFFFF) >> i) & 1) == 1) yield (i + shift)): _*)
  }
  def intToBitSet(x:Int, shift:Int = 0) = {
    BitSet((for (i <- 0 to (32 - 1) if (((x & 0xFFFFFFFF) >> i) & 1) == 1) yield (i + shift)): _*)
  }

  /**
    * bitSet -> Byte(8)/Short(16)/Int(32)
    */
  def bitSetToByte(b:BitSet, sh:Int=0) : Byte = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))}).toByte
  def bitSetToShort(b:BitSet, sh:Int=0) : Short = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))}).toShort
  def bitSetToInt(b:BitSet, sh:Int=0) : Int = ((0 /: b) {(acc, input) => acc + (1 << (input - sh))})

  /**
    * Given bitset & bitWidth, it returns the value in integer type.
    * {0,1,2} & 3 bit => -1
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
