package chitchat.types

import chitchat.types._

import java.lang.{Float => JFloat}

class Float extends Base[JFloat](name = "float") {

  override def encode(value: JFloat): Array[scala.Byte] = ???
  override def decode(byteArray: Array[scala.Byte]): Option[JFloat] = {
    Some(1.234f)
  }
  override def check(value: JFloat): scala.Boolean = ???
}
