package chitchat.types

class Encoding(override val name:String, val elements:Array[Bit]) extends Base(name = name) {
  def size = (0 /: elements)((acc, element) => acc + element.size)

  override def encode(value: Any): Array[Byte] = ???
  override def decode(byteArray: Array[Byte]): Any = {
    List[Int](1,2,3)
  }
}
