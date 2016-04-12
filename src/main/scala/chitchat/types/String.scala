package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
import util.conversion._

class String(override val name:JString = "string", correlatedLabels:Seq[java.lang.String] = Seq[java.lang.String]())
  extends Base[JString](name, correlatedLabels) with Checker {

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

    val string = ByteArrayTool.byteArrayToString(byteArray)
    if (check(string)) Some(string)
    else None
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
