package chitchat.types

import scala.collection.BitSet

/** Abstracts the encoded information such as time/date/location.
  *
  * For example, time has three information in it: hour, minute, and second.
  * Each information has a range (min/max), bit width to represent the value.
  * We use a sequence of Bit to describe the encoding information.
  *
  * @param name
  * @param elements
  */
class Encoding(override val name:java.lang.String, val elements:Seq[Bit]) extends Base[Seq[scala.Int]](name = name) {
  def size = (0 /: elements)((acc, element) => acc + element.size)

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
    * @param bigEndian We do not use endian in encoding class
    * @return array byte to encode the value
    */
  override def encode(value: Seq[scala.Int], bigEndian:scala.Boolean = false): Array[scala.Byte] = {
    // check size
    if (value.size != elements.size) throw new RuntimeException(s"Value count ${value.size} is different from element count ${elements.size}")


    val encodedSeq = elements.zip(value) map {
      case (element, v) => {
        if (!(element.check(v)))
          throw new RuntimeException(s"value ${v} is not in range (${element.min}-${element.max}) of element ${element.name}")
        element.encode(v)
      }
    }

    //stitch(encodedSeq)

    null
  }
  override def decode(byteArray: Array[scala.Byte], bigEndian:scala.Boolean = false): Option[Seq[scala.Int]] = {
    Some(List[scala.Int](1,2,3))
  }
  override def check(value: Seq[scala.Int]): scala.Boolean = ???
}
