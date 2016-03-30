package util.conversion

import java.nio.ByteBuffer

import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{Map => MMap}

/** ByteArrayTool module contains bytearray (Array[Byte]) from/to Scala data types
  * It also provides adjust function to fit the type value in the given bytearray with N elements.
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

  /****************************************************************************
    * Adjust
    ****************************************************************************/

  /** Given value of N bytes array, and given goalSize M >= N, append (M-N) to make the
    * byte array size M.
    *
    * @param value
    * @param goalSize
    * @return Expanded byte array
    */
  def adjust(value:Array[Byte], goalSize:Int, signExtension:Boolean = false) : Array[Byte] = {
    val originalSize = value.size
    if (goalSize == originalSize) return value // nothing to do when the goal size is the same as originalSize
    if (goalSize < originalSize) throw new Exception(s"Goal size (${goalSize}}) should be larger than original size (${originalSize}})")

    var v:Byte = 0
    if (signExtension) {
      // ByteBuffer uses BigEndian, so use the lower bytes to check the sign
      if (value(0) < 0) v = (-1).toByte
    }
    val head = Array.fill[Byte](goalSize - originalSize)(v)
    // value is low bytes where head is high bytes
    value ++ head
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
    * ==== idea ====
    *
    * {{{
    * 1. The byteArray that contains string has the format [Size:String:000000....],
    * 1. we can get from the Size (bytearray(0), we can extract the String.
    * }}}
    *
    * ==== Refer ====
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

  /****************************************************************************
    * Type Int to/from ByteArray
    ****************************************************************************/

  // byte
  def byteToByteArray(x: Byte) = ByteBuffer.allocate(1).put(x).array()
  def byteArrayToByte(x: Array[Byte]) = {
    ByteBuffer.wrap(x).get()
  }

  // short (2 bytes)
  def shortToByteArray(x: Short, bigEndian:Boolean = true) = {
    val res = ByteBuffer.allocate(2).putShort(x).array()
    if (bigEndian) res else res.reverse
  }
  def byteArrayToShort(x: Array[Byte], bigEndian:Boolean = true) = {
    if (x.size < 2) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(if (bigEndian) x else x.reverse).getShort
  }

  // Int (4 bytes)
  /** Returns byte array from the integer value
    *
    * @param x
    * @param bigEndian
    * @return
    */
  def intToByteArray(x: Int, bigEndian:Boolean = true) = {
    val res = ByteBuffer.allocate(4).putInt(x).array()
    if (bigEndian) res else res.reverse
  }

  /** Returns integer value from 4 bytes or more byte array
    *
    * ==== Constraints ====
    * The size of input byte array should be more than 4 bytes.
    *
    * @param x
    * @return
    */
  def byteArrayToInt(x: Array[Byte], bigEndian:Boolean = true) = {
    if (x.size < 4) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(if (bigEndian) x else x.reverse).getInt
  }

  // long (8 bytes)
  def longToByteArray(x: Long, bigEndian:Boolean = true) = {
    val res = ByteBuffer.allocate(8).putLong(x).array()
    if (bigEndian) res else res.reverse
  }
  def byteArrayToLong(x: Array[Byte], bigEndian:Boolean = true) = {
    if (x.size < 8) throw new RuntimeException("input Array[Byte] size is less than four bytes")
    ByteBuffer.wrap(if (bigEndian) x else x.reverse).getLong
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
    * @return generated byteArray
    */
  def bitSetToByteArray(bitSet:BitSet, goalSize:Int = 0) :Array[Byte] = {
    // when there is no input in x:BitSet, should return Array[Byte](0)
    if (bitSet.size == 0) return Array.fill[Byte](goalSize)(0)

    val bits = MMap[Int, Byte]().withDefaultValue(0)
    for (i <- bitSet) {
      val bitLocation = i % 8
      val group = i / 8
      bits(group) = (bits(group) + (1 << bitLocation)).toByte
    }
    val byteArray = Array.fill[Byte](bits.keys.max + 1)(0)

    for ((k,v) <- bits) {
      byteArray(k) = v
    }

    // default goalSize set and adjust
    if (goalSize == 0) {
      byteArray
    } else {
      adjust(byteArray, goalSize = goalSize)
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
    * @param byteArray
    * @return generated BitSet
    */
  def byteArrayToBitSet(byteArray:Array[Byte]) = {
    var res = ArrayBuffer[Int]()
    for ((v,i) <- byteArray.zipWithIndex if v != 0) {
      res.appendAll(BitSetTool.byteToBitSet(v).toArray.map(_ + 8*i))
    }
    scala.collection.immutable.BitSet(res: _*)
  }
}