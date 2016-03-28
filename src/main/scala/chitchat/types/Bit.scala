package chitchat.types

//import grapevineType.BottomType._
//import util.conversion.{ByteArrayTool, BitSetTool}

import scala.collection.BitSet

abstract class Bit {
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