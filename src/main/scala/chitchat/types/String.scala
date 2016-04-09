package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
import util.conversion._

class String(override val name:JString = "string") extends Base[JString](name) with Checker {

  private def charInRange(char:scala.Byte): scala.Boolean = {
    val uchar = 0xFF & char
    (uchar >= 0x20 && uchar <= 0x7E)
  }

  override def encode(value: JString): Option[Array[scala.Byte]] = {
    Some(ByteArrayTool.stringToByteArray(value))
  }

  override def decode(byteArray: Array[scala.Byte]): Option[JString] = {
    val sizeInBytes = byteArray(0) + 1
    val size = sizeInBytes * 8

    // (size + 1) is the total string length, so byteArray should be same or larger than this
    if (byteArray.size < sizeInBytes) return None

    // excessive bytes should be zero
    if (!checkRange(sizeInBytes, byteArray)) return None

    // each of the chars in byteArray should be in range
    byteArray.slice(1, sizeInBytes).foreach { bytearray =>
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

  // size == 0 means the type is String

  override def size: Int = 0

  override def sizeInBytes: Int = 0
}
