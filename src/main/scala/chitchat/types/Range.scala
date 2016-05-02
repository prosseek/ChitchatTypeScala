package chitchat.types

import util.conversion.ByteArrayTool._

/**  *
  * ==== Parameters ====
  *
  * size is defined as `var` as in encoded data type, the value is updated with summing all the sub elements.
  *
  * @param name
  * @param size
  * @param signed
  * @param min
  * @param max
  */
class Range(override val name:java.lang.String = "",
            var size:Int = 0,
            val signed:scala.Boolean = false,
            val min:Int = 0,
            val max:Int = 0
            )
  extends Base[scala.Int](name) with Checker {

  override def sizeInBytes = util.conversion.Util.getBytesForBits(size)

  /** Returns Array[Byte] from the given value
    *
    * ==== Example ====
    * {{{
    *   val e = new Bit("bit", 3, true, max = 3, min = -4)
    *   e.encode(1) => 0:1
    *   e.encode(1, false) => 1:0
    *   e.encode(-4) => 0xF0:0
    *   e.encode(-4, false) => 0:0xF0
    * }}}
    *
    * ==== Algorithm ====
    * 1. Checks if the value is within range
    * 2. Calculate how many bytes are needed for the range (size)
    * 3. Create byte array of 4 bytes (int)
    * 4. Convert it to fit into the bytes range calculated in 2
    *
    * ==== Constraints ====
    * 1. The value is in the range of -2^(31) - (2^(31) - 1)
    *
    * @param value
    * @return
    */
  override def encode(value: Int): Option[Array[scala.Byte]] = {
    if (check(value) == false) {
      throw new RuntimeException(s"Bit.encode value error (check($value)) returns false: min($min)/max($max)")
    }
    val totalBytes = util.conversion.Util.getBytesForBits(size)
    val byteArray = intToByteArray(value) // make 4 bytes data
    Some(util.conversion.ByteArrayTool.adjust(byteArray, goalSize = totalBytes))
  }

  /** Returns the decoded byte array into Int type value
    *
    * ==== Why return Option[scala.Int] type value? ====
    *  - We assume that the encoded value that is recovered is less than 32 bits integer value.
    *  - The Bit type's decode method returns scala.Int.
    *  - When the value is not within range, it should return error condition. Option is used to indicate the condition.
    *
    * @param byteArray
    * @return
    */

  override def decode(byteArray: Array[scala.Byte]): Option[scala.Int] = {
    if(!checkRange(totalBytes = sizeInBytes, byteArray = byteArray)) return None

    // The value should be within range
    val value = byteArray.slice(0, sizeInBytes)
    val adjustedByteArray = util.conversion.ByteArrayTool.adjust(value, goalSize = 4, signExtension = signed)
    val result = byteArrayToInt(adjustedByteArray)
    if (check(result)) Some(result) else None
  }

  override def check(value: Int): scala.Boolean = {
    (value <= max) && (value >= min)
  }
}
