package chitchat.types

import java.lang.{Float => JFloat, String => JString}
import util.conversion.ByteArrayTool

object Float

class Float(override val name: JString = "float", val elements:Seq[JFloat] = null) extends Base[JFloat](name) with Checker {
  def size = 4 * 8
  def adjustValue = 1.0f
  def sizeInBytes = 4

  def adJustedValue(value:JFloat) = if (value > 0) value + adjustValue else value - adjustValue
  def revertedValue(value:JFloat) = if (value > 0) value - adjustValue else value + adjustValue

  override def encode(value: JFloat): Option[Array[scala.Byte]] = {
    var adjustedValue = adJustedValue(value)
    Some(ByteArrayTool.floatToByteArray(adjustedValue))
  }
  override def decode(byteArray: Array[scala.Byte]): Option[JFloat] = {

    if (!checkRange(sizeInBytes, byteArray)) return None

    try {
      val decodedValue: java.lang.Float = ByteArrayTool.byteArrayToFloat(byteArray)

      if (decodedValue.isNaN || decodedValue.isInfinite)
        return None

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
    if (elements == null) true
    else {
      val min = elements(0)
      val max = elements(1)

      (value >= min && value <= max)
    }
  }
}
