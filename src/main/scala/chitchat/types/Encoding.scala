package chitchat.types

class Encoding(override val name:java.lang.String, val elements:Array[Bit]) extends Base[Seq[scala.Int]](name = name) {
  def size = (0 /: elements)((acc, element) => acc + element.size)

  override def encode(value: Seq[scala.Int], bigEndian:scala.Boolean = false): Array[scala.Byte] = ???
  override def decode(byteArray: Array[scala.Byte], bigEndian:scala.Boolean = false): Option[Seq[scala.Int]] = {
    Some(List[scala.Int](1,2,3))
  }
  override def check(value: Seq[scala.Int]): scala.Boolean = ???
}
