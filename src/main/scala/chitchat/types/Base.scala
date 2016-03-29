package chitchat.types

/**
  * Created by smcho on 3/28/16.
  */
abstract class Base(val name:String = "") {
  def encode(value:Any) : Array[Byte]
  def decode(byteArray: Array[Byte]) : Any
}