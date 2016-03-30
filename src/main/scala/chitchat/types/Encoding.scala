package chitchat.types

/** Abstracts the encoded information such as time/date/location.
  *
  * For example, time has three information in it: hour, minute, and second.
  * Each information has a range (min/max), bit width to represent the value.
  * We use a sequence of Bit to describe the encoding information.
  *
  * @param name
  * @param elements
  */
class Encoding(override val name:java.lang.String, val elements:Array[Bit]) extends Base[Seq[scala.Int]](name = name) {
  def size = (0 /: elements)((acc, element) => acc + element.size)

  override def encode(value: Seq[scala.Int], bigEndian:scala.Boolean = false): Array[scala.Byte] = ???
  override def decode(byteArray: Array[scala.Byte], bigEndian:scala.Boolean = false): Option[Seq[scala.Int]] = {
    Some(List[scala.Int](1,2,3))
  }
  override def check(value: Seq[scala.Int]): scala.Boolean = ???
}
