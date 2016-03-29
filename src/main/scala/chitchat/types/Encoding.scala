package chitchat.types

class Encoding(override val name:String, val elements:Array[Bit]) extends Bit(name = name) {
  this.size = (0 /: elements)((acc, element) => acc + element.size)
}
