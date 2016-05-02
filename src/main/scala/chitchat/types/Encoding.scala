package chitchat.types

import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer

/** Abstracts the encoded information such as time/date/location.
  *
  * For example, time has three information in it: hour, minute, and second.
  * Each information has a range (min/max), bit width to represent the value.
  * We use a sequence of Bit to describe the encoding information.
  *
  * @param name
  * @param elements
  */
class Encoding(override val name:java.lang.String, val elements:Seq[Range])
  extends Base[Seq[scala.Int]](name = name) with Checker {

  override def size = (0 /: elements)((acc, element) => acc + element.size)
  def sizes = elements.map(_.size)
  def ranges = elements.map(i => (i.min, i.max))
  def signs = elements.map(i => i.signed)
  override val sizeInBytes = util.conversion.Util.getBytesForBits(size)

  /** Returns byte array from input values in Seq[T] type.
    *
    * ==== Example ====
    * {{{
    *   val e = Encoding("encoding",
    *                    List[Bit](
    *                      new Bit("x", 4, true, max = 3, min = -4),
    *                      new Bit("y", 5, false, max = 3, min = 0)))
    *   val res = encode(List[scala.Int](1,2))
    *   => total 16 bit (2 bytes) byte array x is used for makring patch (|0001|00010|xxxxxxx).
    *                                                                      "x"  "y"   patch
    * }}}
    *
    * ==== Algorithm ====
    *  - Check if the elements and value has the same number of elements.
    *  - Check if each element is within range.
    *
    * @param value Seq[scala.Int] type of data that contains N elements
    * @return array byte to encode the value
    */
  override def encode(value: Seq[scala.Int]): Option[Array[scala.Byte]] = {
    // check size
    if (value.size != elements.size)
      throw new RuntimeException(s"Value count ${value.size} is different from element count ${elements.size}")

    if (check(value) == false)
      throw new RuntimeException(s"value ${value.mkString(s":")} is not in range ${ranges.mkString(":")}")
    val encodedSeq = elements.zip(value) map {
      case (element, v) => {
        element.encode(v).get
      }
    }

    Some(util.conversion.ByteArrayTool.stitch(encodedSeq, sizes))
  }

  override def decode(byteArray: Array[scala.Byte]): Option[Seq[scala.Int]] = {

    if (!checkRange(sizeInBytes, byteArray)) return None

    // byteArrayToBitSet assumes big endian, so added bytes make the wrong bit position
    // ex) XX0000 => byteArrayToBitSeet assumes XX starts in bit 32
    // so we need to remove the added bytes XX0000 => XX
    val slicedByteArray = byteArray.slice(0, sizeInBytes)

    val bitset = util.conversion.ByteArrayTool.byteArrayToBitSet(byteArray = slicedByteArray)

    var sumOfSize = 0
    val elementsInBitset = ArrayBuffer[BitSet]()
    elements.foreach {element =>
      elementsInBitset += bitset.filter(i => i >= sumOfSize && i < (sumOfSize + element.size)).map(_ - sumOfSize)
      sumOfSize += element.size
    }

    // elementsInBitset is in little endian order, so reverse is necessary
    val results = elementsInBitset.zip(elements).map {
      case (bitset, element) =>
        util.conversion.BitSetTool.bitSetToValue(bitset=bitset, bitWidth = element.size, signed = element.signed)
    }

    // check if each element is within the range
    if (check(results))
    //Some(elementsInBitset.toList.zip(sizes).map(i => util.conversion.)
      Some(Seq[scala.Int](results:_*))
    else
      None
  }

  override def check(value: Seq[scala.Int]): scala.Boolean = {
    elements.zip(value) foreach {
      case (element, v) => {
        if (!(element.check(v)))
          return false
      }
    }
    true
  }
}
