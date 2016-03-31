package chitchat.types

import java.lang.{Float => JFloat}
import util.conversion.ByteArrayTool

class Float extends Base[JFloat](name = "float") {
  def size = 4
  def adjustValue = 1.0

  override def encode(value: JFloat): Array[scala.Byte] = {
    var adjustedValue = if (value > 0) value + adjustValue else value - adjustValue
    ByteArrayTool.floatToByteArray(value)
  }
  override def decode(byteArray: Array[scala.Byte]): Option[JFloat] = {
    if (byteArray.size != 4) throw new RuntimeException(s"The size of byte array is not 4 (${byteArray.size}")
    val decodedValue = ByteArrayTool.byteArrayToFloat(byteArray)
    if (decodedValue > 0) Some((decodedValue - adjustValue).toFloat)
                     else Some((decodedValue + adjustValue).toFloat)
  }
  override def check(value: JFloat): scala.Boolean = true
}
