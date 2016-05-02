package chitchat.types

import java.lang.{Float => JFloat, String => JString}
import util.conversion.ByteArrayTool

object Float

class Float(override val name: JString = "float",
            val min: JFloat = 0.0f,
            val max: JFloat = 0.0f,
            val shift:JFloat = 0.0f
            )
  extends Base[JFloat](name) with Checker {
  override def size = 4 * 8
  override def sizeInBytes = 4

  def adjustedValue(value:JFloat) = if (value > 0) value + shift else value - shift
  def revertedValue(value:JFloat) = if (value > 0) value - shift else value + shift

  override def encode(value: JFloat): Option[Array[scala.Byte]] = {

    if (!check(value)) return None
    var shifted = adjustedValue(value)
    Some(ByteArrayTool.floatToByteArray(shifted))
  }
  override def decode(byteArray: Array[scala.Byte]): Option[JFloat] = {

    if (!checkRange(sizeInBytes, byteArray)) return None

    try {
      val decodedValue: java.lang.Float = ByteArrayTool.byteArrayToFloat(byteArray)

      if (decodedValue.isNaN || decodedValue.isInfinite)
        return None

      // check if decoded value is in the shift range
      if (shift != 0.0f) {
        if (decodedValue >= 0.0) if (decodedValue < shift) return None
        if (decodedValue < 0.0)  if (decodedValue > -shift) return None
      }

      val rv = revertedValue(decodedValue)
      if (check(rv))
        Some(rv)
      else
        None
    }
    catch  {
      case e:Exception => None
    }
  }
  override def check(value: JFloat): scala.Boolean = {
    if (min == max && min == 0.0f) true
    else {
      (value >= min && value <= max)
    }
  }
}
