package util

import java.nio.ByteBuffer

import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{Map => MMap}

import util.Util._

/**
  * ByteArrayTool module contains bytearray (Array[Byte]) from/to Scala data types
  * It also provides adjust function to fit the type value in the given bytearray with N elements.
  *
  * 1. String (Pascal string)
  * 1.1 stringToByteArray: "hello" -> [5Hello]
  * 2.
  */

object ByteArrayTool {

  /****************************************************************************
    * Adjust
    ****************************************************************************/

  /**
    * Given value of N bytes array, and given goalSize M >= N, append (M-N) to make the
    * byte array size M.
    *
    * Why: In BF table of 8 bytes width, 4 bytes integer should be prepended 4 bytes.
    *
    * constraints:
    *    goalSize should be the same or larger than the array size.
    *
    * @param value
    * @param goalSize
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
    val size = (x.size + 1)
    if (size > 255) throw new RuntimeException(s"String length of ${size} is over 255 bytes")
    Array[Byte](unsignedToByte(x.size)) ++ x.getBytes()
  }

  /**
    * String -> ByteArray with n elements
    *
    * constraint1: the maximum string width is 255, so n should not be negative number & less than 256 (0 <= x < 256)
    * constraint2: the first element of the array is the width of string, so the goalWidth - 1 should be equal or larger
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
    Array[Byte](unsignedToByte(stringLength)) ++ inputString.getBytes() ++ new Array[Byte](diff)
  }

  /**
    * byteArray -> String
    *
    * How: the byteArray that contains string has the format [Size:String:000000....]
    *      From the Size (bytearray(0), we can extract the String.
    *
    * Refer: detect the location of 0
    * http://stackoverflow.com/questions/23976309/trimming-byte-array-when-converting-byte-array-to-string-in-java-scala
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

  /*
    from Data : T -> ByteArray
   */
  // int
  def intToByteArray(x: Int) = ByteBuffer.allocate(4).putInt(x).array()
  def intToByteArray(x: Int, size: Int) : Array[Byte] = {
    val res = intToByteArray(x)
    adjust(value = res, goalSize = size, signExtension = true)
  }

  // short
  def shortToByteArray(x: Short) = ByteBuffer.allocate(2).putShort(x).array()
  def shortToByteArray(x: Short, size: Int) : Array[Byte] = {
    val res = shortToByteArray(x)
    adjust(value = res, goalSize = size, signExtension = true)
  }

  // long
  def longToByteArray(x: Long) = ByteBuffer.allocate(8).putLong(x).array()
  def longToByteArray(x: Long, size: Int) : Array[Byte] = {
    val res = longToByteArray(x)
    adjust(value = res, goalSize = size, signExtension = true)
  }

  // byte
  def byteToByteArray(x: Byte) = ByteBuffer.allocate(1).put(x).array()
  def byteToByteArray(x: Byte, size: Int) : Array[Byte] = {
    val res = byteToByteArray(x)
    adjust(value = res, goalSize = size, signExtension = true)
  }

  // double
  def doubleToByteArray(x: Double) = {
    val l = java.lang.Double.doubleToLongBits(x)
    longToByteArray(l)
  }

  def doubleToByteArray(x: Double, size:Int = 8) = {
    if (size < 8) throw new Exception(s"Double data should be at least 8 bytes, but given ${size}")
    val l = java.lang.Double.doubleToLongBits(x)
    adjust(longToByteArray(l), goalSize = size)
  }

  // float
  def floatToByteArray(x: Float) = {
    val l = java.lang.Float.floatToIntBits(x)
    intToByteArray(l)
  }
  def floatToByteArray(x: Float, size:Int = 4) = {
    if (size < 4) throw new RuntimeException(s"Float data should be at least 4 bytes, but given ${size}")
    val l = java.lang.Float.floatToIntBits(x)
    adjust(intToByteArray(l), goalSize = size)
  }

  // byte array
  def byteArrayToBitSet(x:Array[Byte]) = {
    var res = ArrayBuffer[Int]()
    for ((v,i) <- x.zipWithIndex if v != 0) {
      res.appendAll(BitSetTool.byteToBitSet(v).toArray.map(_ + 8*i))
    }
    scala.collection.immutable.BitSet(res: _*)
  }

  def bitSetToByteArray(x:BitSet, goalSize:Int = -1) :Array[Byte] = {
    // bug [2014/08/23]
    // when there is no input in x:BitSet, should return Array[Byte](0)
    if (x.size == 0) return Array[Byte](0)

    val bits = MMap[Int, Byte]().withDefaultValue(0)
    for (i <- x) {
      val bitLocation = i % 8
      val group = i / 8
      bits(group) = (bits(group) + (1 << bitLocation)).toByte
    }
    val byteArray = Array.fill[Byte](bits.keys.max + 1)(0)

    for ((k,v) <- bits) {
      byteArray(k) = v
    }

    // default goalSize set and adjust
    if (goalSize == -1) {
      byteArray
    } else {
      adjust(byteArray, goalSize = goalSize)
    }
  }

  // byte
  /**
   * ByteBuffer.wrap(x).get() -> Returns the value (Byte)
   * @param x
   * @return
   */
  def byteArrayToByte(x: Array[Byte]) = {
    ByteBuffer.wrap(x).get()
  }

  def byteArrayToShort(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getShort
  }

  // int
  def byteArrayToInt(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getInt
  }

  // long
  def byteArrayToLong(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getLong
  }

  def byteArrayToDouble(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getDouble
  }

  // float
  def byteArrayToFloat(x: Array[Byte]) = {
    ByteBuffer.wrap(x).getFloat
  }
}

