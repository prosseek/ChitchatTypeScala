package util.conversion

import java.nio.ByteBuffer

import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{Map => MMap}

/** ByteArrayTool module contains bytearray (Array[Byte]) from/to Scala data types
  * It also provides adjust function to fit the type value in the given bytearray with N elements.
  *
  *  - Utility
  *    - zeroPatch
  *    - adjust
  *    - stitch
  *
  *  - String (Pascal string)
  *    - stringToByteArray: `"hello" -> [5Hello]`
  *    - byteArrayToString: `5Hello.... -> "Hello"`
  *
  *  - Int
  *     - Long (8 bytes)
  *     - Int (4 bytes)
  *     - Byte (1 byte)
  *     - Short (2 bytes)
  *
  *  - Double
  *     - Double (8 bytes)
  *     - Float (4 bytes)
  *
  *  - BitSet
  *
  */

object ByteArrayTool {

  /**
    *
    * @param byteArray
    * @param goalSize
    */
  def zeroPatch(byteArray:Array[Byte], goalSize:Int): Array[Byte] = {
    val originalSize = byteArray.size
    if (goalSize == originalSize) return byteArray
    if (goalSize < originalSize) throw new RuntimeException(s"Goal size ${goalSize} is smaller than byte array size ${byteArray.size}")

    val zeros = Array.fill[Byte](goalSize - originalSize)(0)
    byteArray ++ zeros
  }

  /****************************************************************************
    * Adjust
    ****************************************************************************/

  /** Returns sign preserving,
    *
    * ==== Example ====
    * {{{
    * // shrink + signed + big endian
    * x = 0:0:0:0:1 (value = 1, 5 bytes, signed, big endian)
    *     ----- <- removed
    * goal = 2 (shirink it into two bytes)
    * adjust(x, 2) => 0:1
    *
    * x = 0xFF:0xFE (value = -2, 2 bytes, signed, big endian)
    * goal = 1 byte (shirink it into one byte)
    * adjust(x, 1) => 0xFE (still -2)
    *
    * // expand + signed + big endian
    * x = 0:1 (value = 1, 2 bytes, signed, big endian)
    * goal = 5 (expand it into five bytes)
    * adjust(x, 5) => 0:0:0:0:1
    *                 ------ <- added
    *
    * x = 0xFF:0xFE (value = -2, 2 bytes, signed, big endian)
    * goal = 5 byte (expand it into five byte)
    * adjust(x, 5) => 0xFF:0xFF:0xFF:0xFF:0xFE (still -2)
    *                 -------------- <- aded
    *
    * // shrink + signed + little endian
    * x = 1:0:0:0:0 (value = 1, 5 bytes, signed, little endian)
    *        ------ <- removed
    * goal = 2 (shirink it into two bytes)
    * adjust(x, 2) => 1:0
    *
    * x = 0xFE:0xFF (value = -2, 2 bytes, signed, little endian)
    *          ---- <- removed
    * goal = 1 byte (shirink it into one byte)
    * adjust(x, 1) => 0xFE (still -2)
    *
    * // expand + signed + little endian
    * x = 1:0 (value = 1, 2 bytes, signed, little endian)
    * goal = 5 (expand it into five bytes)
    * adjust(x, 5) => 1:0:0:0:0
    *                     ----- <- added
    * x = 0xFE:0xFF (value = -2, 2 bytes, signed, little endian)
    * goal = 5 byte (expand it into five byte)
    * adjust(x, 5) => 0xFE:0xFF:0xFF:0xFF:0xFF (still -2)
    *                           -------------- <- added
    * }}}
    *
    *
    * ==== Algorithm ====
    *  1. Check if bytearray size is 0 or goal size is 0
    *  2. If shrink remove the MSBs.
    *     1. If big endian, MSBs are lower bytes (thus big endian).
    *     2. If little endian, MSBs are higher bytes.
    *  3. If expand add 0x00 to the higher bytes with positive value, and add 0xFF with negative value.
    *
    * ==== Discussion ====
    * The algorithm removes the high bits when shrinking the bytearray.
    * This is OK in practical as this function is used functions such as [[chitchat.types.Range.encode]]
    * The [[chitchat.types.Range.encode]] functions reduces the bits only when the value to be encoded is within min/max.
    * The min/max value is smaller than the shrinked byte array.
    *
    * ==== Warning ====
    * 1. This function provides sign preserving expansion of byte array. It means the position of original bits are moved with big endian encoding.
    * 2. With negative value, newly expanded data is filled with 0xFF.
    *
    * @param value
    * @param goalSize
    * @return Expanded byte array
    */
  def adjust(value:Array[Byte], goalSize:Int, signExtension:Boolean = false, bigEndian:Boolean = true) : Array[Byte] = {

    // 1. check bytearray size and goal size
    val originalSize = value.size
    if (originalSize < 1 || goalSize < 1)
      throw new Exception(s"origninal size and goal size should be more than one: goalSize($goalSize)/byteArraySize($originalSize)")
    if (goalSize == originalSize) return value // nothing to do when the goal size is the same as originalSize

    // shrink - remove the MSBs, it means slice the LSBs
    if (goalSize < originalSize) {
      // with little endian, MSBs are high bits, so LSBs are low bits
      // case bigEndian => [orig - goalSize:orig] => orig points to the last item
      // case littleEndian => [0:goalSize]
      val start = if (bigEndian) (originalSize - goalSize) else 0
      value.slice(start, start + goalSize)
    }
    // expand - add to MSBs
    else {
      var v: Byte = 0
      if (signExtension) {
        val sign = if (bigEndian) (value(0) < 0) else (value(originalSize-1) < 127)
        // ByteBuffer uses BigEndian, so use the lower bytes to check the sign
        if (sign == true) v = -1
      }
      val head = Array.fill[Byte](goalSize - originalSize)(v)

      if (bigEndian) head ++ value else value ++ head
    }
  }

  /** Returns bit adjusted bytearrays.
    *
    * ==== Why ====
    * Encoded value from Bit.encode() method is byte aligned.
    * However, when we aggregate multiple encoded values, we need to adjust them to save bytes.
    *
    * ==== Example ====
    * {{{
    *   val a = Bit("a", 5, false, 0, 2)
    *   val b = Bit("b", 6, false, 0, 1)
    *
    *   Let's say we encode the value with big endian (default)
    *
    *   a.encode(2) => XXX00010
    *   b.endode(1) => XX000001
    *
    *   The stiched bytearray uses little endian.
    *
    *   stich([a.encoded(1), b.encode(2)], [5,6]) => 01000100000PPPPP
    *                                       aaaaabbbbbb-----
    * }}}
    *
    * ==== Algorithm ====
    * 1. change byte array -> BitSet
    * 2. add offset that starts with 0, the Nth offset starts from the sum of previous byte array sizes
    * 3. revert it back to byte array
    *
    * @param byteArrays
    * @param sizes
    */
  def stitch(byteArrays: Seq[Array[scala.Byte]], sizes: Seq[Int]) = {

    var res = BitSet()
    var totalSize = 0
    byteArrays.zip(sizes) foreach {
      case (byteArray, size) => {
        val r = byteArrayToBitSet(byteArray = byteArray)
        // add the bits only smaller than size
        // ex) size == 3, r == (0,1,2,3,4), we only use (0,1,2)
        val modifiedValue = r.filter( _ < size).map(_ + totalSize)
        res ++= modifiedValue
        totalSize += size
      }
    }
    //println(res.mkString(":"))
    val goalSize = util.conversion.Util.getBytesForBits(totalSize)
    bitSetToByteArray(res, goalSize)
  }

  /******************************************************************
    * Pascal type string to/from byte array
    * "Hello" --> 5 (size of the string) + Hello => total 6 bytes of string
    ******************************************************************/

  /**
    * String -> ByteArray
    *
    * @param x
    * @return ByteArray that contains the string x
    */
  def stringToByteArray(x: String) = {
    val stringSize = x.size
    val arraySize = (stringSize + 1)
    if (stringSize > 255) throw new RuntimeException(s"String length of ${stringSize} is over 255 bytes")
    Array[Byte]((x.size & 0xFF).toByte) ++ x.getBytes()
  }

  /**
    * String -> ByteArray with n elements
    *
    * ==== Constraints ====
    *   1. the maximum string width is 255, so n should not be negative number & less than 256 `(0 <= x < 256)`
    *   1. the first element of the array is the width of string, so the goalWidth - 1 should be equal or larger
    *              than the inputString width
    *
    * @param inputString
    * @param goalWidth
    * @return - converted byte array with goalWidth width
    */
  def stringToByteArray(inputString: String, goalWidth:Int) = {
    if (goalWidth > 255 || goalWidth < 0)
      throw new RuntimeException(s"Byte array length of ${goalWidth} is over 255 bytes or below zero")
    val stringLength = inputString.size
    if (stringLength > goalWidth - 1) // -1 is needed for the first byte to store string size
      throw new RuntimeException(s"String length of ${stringLength} smaller than byte array length ${goalWidth}")

    val diff = goalWidth - stringLength - 1
    Array[Byte]((stringLength & 0xFF).toByte) ++ inputString.getBytes() ++ new Array[Byte](diff)
  }

  /**
    * byteArray -> String
    *
    * ==== Idea ====
    *
    * {{{
    * 1. The byteArray that contains string has the format [Size:String:000000....],
    * 1. we can get from the Size (bytearray(0), we can extract the String.
    * }}}
    *
    * ==== References ====
    *
    *  - detect the location of 0: [[http://stackoverflow.com/questions/23976309/trimming-byte-array-when-converting-byte-array-to-string-in-java-scala]]
    *
    * @param byteArray
    * @return string from the input byteArray
    */
  def byteArrayToString(byteArray: Array[Byte]) = {
    //
    val size = byteArray(0) & 0xFF
    if (byteArray.size - 1 < size) // (x.size - 1 >= size) should be met
      throw new RuntimeException(s"byte array size(${byteArray.size}} - 1) is smaller than (size)(${size}) ")
    new String(byteArray.slice(1, size + 1), "ASCII")
  }

  // byte
  def byteToByteArray(x: Byte) = ByteBuffer.allocate(1).put(x).array()
  def byteArrayToByte(x: Array[Byte]) = {
    ByteBuffer.wrap(x).get()
  }

  // short (2 bytes)
  def shortToByteArray(x: Short) = {
    ByteBuffer.allocate(2).putShort(x).array()
  }
  def byteArrayToShort(x: Array[Byte]) = {
    if (x.size < 2) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(x).getShort
  }

  // Int (4 bytes)
  /** Returns byte array from the integer value
    *
    * @param x
    * @return
    */
  def intToByteArray(x: Int) = {
    ByteBuffer.allocate(4).putInt(x).array()
  }

  /** Returns integer value from 4 bytes or more byte array
    *
    * ==== Constraints ====
    * The size of input byte array should be more than 4 bytes.
    *
    * @param x
    * @return
    */
  def byteArrayToInt(x: Array[Byte]) = {
    if (x.size < 4) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(x).getInt
  }

  // long (8 bytes)
  def longToByteArray(x: Long) = {
    ByteBuffer.allocate(8).putLong(x).array()
  }
  def byteArrayToLong(x: Array[Byte]) = {
    if (x.size < 8) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(x).getLong
  }

  /****************************************************************************
    * Type Double to/from ByteArray
    ****************************************************************************/

  /** Returns double from the input type of byte array
    *
    * @param x
    * @param size
    * @return generated double value
    */
  def doubleToByteArray(x: Double, size:Int = 8) = {
    if (size < 8) throw new Exception(s"Double data should be at least 8 bytes, but given ${size}")
    val l = java.lang.Double.doubleToLongBits(x)
    adjust(longToByteArray(l), goalSize = size)
  }
  def byteArrayToDouble(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getDouble
  }

  // float
  def floatToByteArray(x: Float, size:Int = 4) = {
    if (size < 4) throw new RuntimeException(s"Float data should be at least 4 bytes, but given ${size}")
    val l = java.lang.Float.floatToIntBits(x)
    adjust(intToByteArray(l), goalSize = size)
  }
  def byteArrayToFloat(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getFloat
  }

  /****************************************************************************
  * BitSet to/from ByteArray
  ****************************************************************************/
  /** returns a byte array from bitSet
    *
    * ==== idea ====
    *
    * {{{
    * {0, 8, 9, 10, 16} => 0:7:1 (when goalSize is 3 bytes)
    * }}}
    *
    * ==== Algorithm ====
    *
    * {{{
    * 1. Check the special case when there is no 0 => return ByteArray of 0's
    *    When no goalSize is given, return empty Array[Byte]
    * 2. For each element i in the BitSet
    *    i / 8 is the Nth element in the array (let's call it group)
    *    i % 8 is the bitLocation
    * 3. Bit set is the addition of shifted value
    *    Making 10001000 (low to high) =>
    *           --------
    *           01234567
    *    is (1 << 0 (0 bit shift) + 1 << 4 (7 bit shift) => 2^0 (1) + 2^4 (16) = 17
    *
    *    Create a map (group, sum of shifted values in the group)
    * 4. From the map, the maximum value of group + 1 is the size of the bytearray
    *    as the group starts from 0
    * 5. Make the byte array and set the array with (group, shifted value)
    * }}}
    *
    * @param bitSet
    * @param goalSize
    * @param bigEndian
    * @return generated byteArray
    */
  def bitSetToByteArray(bitSet:BitSet, goalSize:Int = 0, bigEndian:Boolean = true) :Array[Byte] = {
    // when there is no input in x:BitSet, should return Array[Byte](0)
    if (bitSet.size == 0) return Array.fill[Byte](goalSize)(0)

    val bits = MMap[Int, Byte]().withDefaultValue(0)
    for (i <- bitSet) {
      val bitLocation = i % 8
      val group = i / 8
      bits(group) = (bits(group) + (1 << bitLocation)).toByte
    }
    val maxByte = bits.keys.max
    val byteArray = Array.fill[Byte](maxByte + 1)(0)

    for ((group,v) <- bits) {
      if (bigEndian)
        byteArray(maxByte - group) = v
      else
        byteArray(group) = v
    }

    // default goalSize set and adjust
    if (goalSize == 0) {
      byteArray
    } else {
      // bitset does not know about sign, so it should be set as false
      adjust(byteArray, goalSize = goalSize, signExtension = false, bigEndian = bigEndian)
    }
  }

  /** Returns bitSet from byte array
    *
    * ==== idea ====
    * {{{
    * Given 0:0:0:1 (low to high), the location of the 1 is
    * 0...0 (lower 8x3 = 24 bit) 10000000 (first of the last byte)
    *
    * In this example, the 24 + 0 = {24} will be returned
    * }}}
    *
    * ==== algorithm ====
    *
    * {{{
    * 1. get a pair of (value, index) => (0,0) (0,1) (0,2) (1,3)
    *    the index multiplied by 8 gives the added location of the 1
    * 2. byteToBitSet is used to get the BitSet for each byte
    *    the index*8 is added to the results
    * }}}
    *
    * ==== Why shift parameter? ====
    *  - [[util.conversion.ByteArrayTool.stitch]] function calls this function.
    *    The values should be shifted for the stitch operation.
    *
    * @param byteArray
    * @param shift
    * @return generated BitSet
    */
  def byteArrayToBitSet(byteArray:Array[Byte], shift:Int = 0) = {

    // we use only big endian, so to make bitset we need to reveres
    val arrangedByteArray = byteArray.reverse

    var res = ArrayBuffer[Int]()
    for ((v,i) <- arrangedByteArray.zipWithIndex if v != 0) {
      res.appendAll(BitSetTool.byteToBitSet(value = v, shift = shift).toArray.map(_ + 8*i))
    }
    scala.collection.immutable.BitSet(res: _*)
  }
}