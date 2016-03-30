package chitchat.types

import java.lang.{String => JString}
/**
  * Created by smcho on 3/28/16.
  */
class String extends Base[JString](name = "string") {

  override def encode(value: JString, bigEndian:scala.Boolean = false): Array[scala.Byte] = ???
  override def decode(byteArray: Array[scala.Byte], bigEndian:scala.Boolean = false): Option[JString] = {
    Some("hello")
  }
  override def check(value: JString): scala.Boolean = ???
}
