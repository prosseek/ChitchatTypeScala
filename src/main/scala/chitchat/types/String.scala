package chitchat.types

import chitchat.types._

import java.lang.{String => JString}
import util.conversion._

class String(override val name:JString = "string",
             min:scala.Char = 0.toChar,
             max:scala.Char = 0.toChar,
             conditions:Seq[Any] = Seq[Any]())
  extends Base[JString](name) with Checker {

  private def charInRange(char:scala.Byte): scala.Boolean = {
    val uchar = 0xFF & char
    (uchar >= 0x20 && uchar <= 0x7E)
  }

  /**
    * range:Seq[String] has two elements
    *
    * for two element
    *   range[0] = min
    *   range[1] = max
    *
    * @return
    */
  def checkRange(char:scala.Byte) = {
    if (min == max && min == 0.toChar) true
    else {
        char <= max && char >= min
    }
  }

  /** condition checks
    *
    * 1. length = condition(0)
    * 2. count = condition(1)
    *
    * @param input
    * @return
    */
  def checkCondition(input:JString) = {
    if (conditions.length == 0) true
    else {
      val function_name = conditions(0)
      val value = conditions(1).asInstanceOf[Int]

      if (function_name == "maxlength") {
        input.length <= value
      }
      else if (function_name == "minlength") {
        input.length >= value
      }
      else
        throw new RuntimeException(s"only maxlength/minlength is supported")
    }
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
    if (!checkCondition(value)) return false
    value.foreach { char =>
      if (!checkRange(char.toByte)) return false
      if (!charInRange(char.toByte)) return false
    }
    true
  }

  // size == 0 means the type is String
  override def size: Int = 0

  override def sizeInBytes: Int = 0
}
