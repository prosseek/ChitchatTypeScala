package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
import util.conversion._

class String extends Base[JString](name = "string") {
  override def encode(value: JString): Array[scala.Byte] = {
    ByteArrayTool.stringToByteArray(value)
  }

  override def decode(byteArray: Array[scala.Byte]): Option[JString] = {
    val res = ByteArrayTool.byteArrayToString(byteArray)
    Some(res)
  }
  override def check(value: JString): scala.Boolean = {
    value.foreach { char =>
      if (!(char >= 0x20 && char <= 0x7E)) return false
    }
    true
  }
}
