package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
/**
  * Created by smcho on 3/28/16.
  */
class String extends Base[JString](name = "string") {

  override def encode(value: JString): Array[scala.Byte] = ???
  override def decode(byteArray: Array[scala.Byte]): Option[JString] = {
    Some("hello")
  }
  override def check(value: JString): scala.Boolean = ???
}
