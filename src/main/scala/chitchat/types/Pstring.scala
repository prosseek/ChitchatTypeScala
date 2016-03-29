package chitchat.types

/**
  * Created by smcho on 3/28/16.
  */
class Pstring extends Base(name = "string") {

  override def encode(value: Any): Array[Byte] = ???
  override def decode(byteArray: Array[Byte]): Any = {
    "hello"
  }
}
