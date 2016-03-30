package chitchat.types

import chitchat.types.{Byte => CByte}
import util.conversion.ByteArrayTool._

/**  *
  * ==== Parameters ====
  *
  * size is defined as `var` as in encoded data type, the value is updated with summing all the sub elements.
  *
  * @param name
  * @param size
  * @param signed
  * @param min
  * @param max
  */
class Bit(override val name:java.lang.String = "",
                   var size:Int = 0,
                   val signed:scala.Boolean = false,
                   val min:Int = 0,
                   val max:Int = 0) extends Base[scala.Int](name) {

  /** Returns Array[Byte] from the given value
    *
    * ==== Example ====
    * {{{
    *   val e = new Bit("bit", 3, true, max = 3, min = -4)
    *   e.encode(1) => 0:1
    *   e.encode(1, false) => 1:0
    *   e.encode(-4) => 0xF0:0
    *   e.encode(-4, false) => 0:0xF0
    * }}}
    *
    * ==== Algorithm ====
    * 1. Checks if the value is within range
    * 2. Calculate how many bytes are needed for the range (size)
    * 3. Create byte array of 4 bytes (int)
    * 4. Convert it to fit into the bytes range calculated in 2
    *
    * ==== Constraints ====
    * 1. The value is in the range of -2^(31) - (2^(31) - 1)
    *
    * @param value
    * @return
    */
  override def encode(value: Int, bigEndian:scala.Boolean = true): Array[scala.Byte] = {
    if (check(value) == false) {
      throw new RuntimeException(s"Bit.encode value error (check($value)) returns false: min($min)/max($max)")
    }
    val totalBytes = util.conversion.Util.getBytesForBits(size)
    val byteArray = intToByteArray(value, bigEndian = bigEndian) // make 4 bytes data
    util.conversion.ByteArrayTool.adjust(byteArray, goalSize = totalBytes, bigEndian = bigEndian)
  }

  /** Returns the decoded byte array into Int type value
    *
    * ==== Why return scala.Int type value? ====
    *  - We assume that the encoded value that is recovered is less than 32 bits integer value.
    *  - The Bit type's decode method returns scala.Int.
    *
    * @param byteArray
    * @param bigEndian
    * @return
    */

  override def decode(byteArray: Array[scala.Byte], bigEndian:scala.Boolean = true): Option[scala.Int] = {
    val totalBytes = util.conversion.Util.getBytesForBits(size)
    if (totalBytes > byteArray.size)
      throw new RuntimeException(s"byteArray size ${byteArray.size} is smaller to represent the vale in ${totalBytes}")
    val adjustedByteArray = util.conversion.ByteArrayTool.adjust(byteArray, goalSize = 4, signExtension = signed, bigEndian = bigEndian)
    val result = byteArrayToInt(adjustedByteArray)
    if (check(result)) Some(result) else None
  }

  override def check(value: Int): scala.Boolean = {
    (value <= max) && (value >= min)
  }
}

/*
  var bits:List[Int] = _
  var ranges:List[(Int,Int)] = _
  var signed = true

  def getValue(value:Int, bits:Int, signed:Boolean) = {
    val mask = math.pow(2,bits).toInt - 1
    if (signed)
      value
    else { // unsigned
      value & mask
    }
  }
  def getValues(values:List[Int], bits:List[Int], signed:Boolean) = {
    values.zip(bits) map {case (v, b) => getValue(v, b, signed)}
  }

  // http://stackoverflow.com/questions/25319546/adding-a-list-from-different-starting-point-in-scala
  def psum (index:Int, bits:List[Int]) = bits.drop(index).sum

  // http://stackoverflow.com/questions/25315590/checking-values-in-lists-with-scala
  // check(aValue, a._2, a._3) && check (bValue, b._2, b._3) && check(cValue, c._2, c._3)
  def check(values:List[Int], ranges:List[(Int, Int)]) : Boolean = {
    (values zip ranges).forall{case (a,(b,c)) => check(a,b,c)}
  }

  //  BitSetTool.intToBitSet(values(0), bits(1) + bits(2) + 0) ++
  //  BitSetTool.intToBitSet(values(1),           bits(2) + 0) ++
  //  BitSetTool.intToBitSet(values(2),                   + 0)
  def shiftAndJoin(values:List[Int], bits:List[Int]) = {
    // http://stackoverflow.com/questions/25319725/aggregation-of-bitset-collection-data-in-scala
    (values zipWithIndex) map { case (v, i) =>
      val mask = (scala.math.pow(2, bits(i)).toInt - 1)
      BitSetTool.intToBitSet(v & mask, psum(i+1, bits)) } reduce {_ ++ _}
  }

  //  val c = bs.filter(v => v >= psum(3, bits) && v < psum(2, bits)).map(_ - psum(3, bits)) <- lower bits
  //  val b = bs.filter(v => v >= psum(2, bits) && v < psum(1, bits)).map(_ - psum(2, bits))
  //  val a = bs.filter(v => v >= psum(1, bits) && v < psum(0, bits)).map(_ - psum(1, bits)) <- higher bits
  def splitBitSets(bs:BitSet, bits:List[Int]) : List[BitSet]= {
    // bug [2014/08/23]
    // this method returned only the bitsets within the bit range
    // but it should also return the outside the range, as it indicates the Computational bottom
    val res = (List[BitSet]() /: Range(0, bits.size)) { (acc, index) =>
      acc ++ List(bs.filter(v => v >= psum(index+1, bits) && v < psum(index, bits)).map(_ - psum(index+1, bits)))
    }
    if (bs.filter(_ >= bits.sum).size == 0)
      res
    else
      res ++ List(bs.filter(_ >= bits.sum).map(_ - bits.sum)) // we may don't need map(_ - psum...) code as this means the error
  }
  override def getSize = getBytes(bits)

  def fromByteArray(ba: Array[Byte], byteSize:Int): BottomType = {
    if (ByteArrayTool.byteArrayToBitSet(ba).filter(_ >= bits.sum).size > 0) Computational
    else super.fromByteArray(ba, byteSize)
  }
}
*/