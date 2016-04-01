package chitchat.types

import java.lang.{Float => JFloat}
import util.conversion.ByteArrayTool

class Float extends Base[JFloat](name = "float") with Checker {
  def size = 4 * 8
  def adjustValue = 1.0
  def sizeInBytes = 4

  override def encode(value: JFloat): Array[scala.Byte] = {
    var adjustedValue = if (value > 0) value + adjustValue else value - adjustValue
    ByteArrayTool.floatToByteArray(value)
  }
  override def decode(byteArray: Array[scala.Byte]): Option[JFloat] = {

    if (!checkRange(sizeInBytes, byteArray)) return None

    val decodedValue = ByteArrayTool.byteArrayToFloat(byteArray)
    if (decodedValue > 0) Some((decodedValue - adjustValue).toFloat)
                     else Some((decodedValue + adjustValue).toFloat)
  }
  override def check(value: JFloat): scala.Boolean = true
}
