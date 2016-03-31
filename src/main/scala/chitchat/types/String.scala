package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
import util.conversion._

class String extends Base[JString](name = "string") {

  private def charInRange(char:scala.Byte): scala.Boolean = {
    val uchar = 0xFF & char
    (uchar >= 0x20 && uchar <= 0x7E)
  }

  override def encode(value: JString): Array[scala.Byte] = {
    ByteArrayTool.stringToByteArray(value)
  }

  override def decode(byteArray: Array[scala.Byte]): Option[JString] = {
    val size = byteArray(0)

    // (size + 1) is the total string length, so byteArray should be same or larger than this
    if (byteArray.size < (size + 1)) return None

    // each of the chars in byteArray should be in range
    byteArray.slice(1, byteArray.size).foreach { bytearray =>
      if (!charInRange(bytearray)) return None
    }
    Some(ByteArrayTool.byteArrayToString(byteArray))
  }
  override def check(value: JString): scala.Boolean = {
    value.foreach { char =>
      if (!charInRange(char.toByte)) return false
    }
    true
  }
}
