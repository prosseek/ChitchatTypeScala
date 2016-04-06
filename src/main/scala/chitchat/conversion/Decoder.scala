package chitchat.conversion

import java.lang.{String => JString}

import scala.{Byte => SByte}
import util.types.TypeInference
import chitchat.types._

class Decoder (val typeInference: TypeInference) {
  /** Returns value from byte array
    *
    * ==== Example ====
    * {{{
    *   "float", Instance_of_float, -4:2:45:11 => 12.34f
    *   "string", 5Hello, 5:H:e:l:l:o => "Hello"
    * }}}
    *
    * @param valueType
    * @param instance
    * @param ba
    * @return
    */
  def decode(valueType:JString, instance:Base[_], ba:Array[Byte]) : Option[Any] = {
    valueType match {
      case "float" => {
        val result = util.conversion.ByteArrayTool.byteArrayToFloat(ba)
        Some(result)
      }
      case "string" => {
        val result = util.conversion.ByteArrayTool.byteArrayToString(ba)
        Some(result)
      }
      case "encoding" => {
        val encodingInstance = instance.asInstanceOf[Encoding]
        encodingInstance.decode(ba)
      }
      case "range" => {
        val rangeInstance = instance.asInstanceOf[Range]
        rangeInstance.decode(ba)
      }
      case _ => {
        throw new RuntimeException(s"Unknown type ${valueType}")
      }
    }
  }
}
