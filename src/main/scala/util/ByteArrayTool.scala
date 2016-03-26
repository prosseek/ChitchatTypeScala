package util

import java.nio.ByteBuffer

import scala.collection.BitSet
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.{Map => MMap}

object ByteArrayTool {
  /**
   * Add more bytes to the value:Array[Byte]
   *
   * @param value
   * @param originalSize
   * @param goalSize
   */
  def adjust(value:Array[Byte], originalSize:Int, goalSize:Int, signExtension:Boolean = false) : Array[Byte] = {
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

  /*
    from Data : T -> ByteArray
   */
  // int
  def intToByteArray(x: Int) = ByteBuffer.allocate(4).putInt(x).array()
  def intToByteArray(x: Int, size: Int) : Array[Byte] = {
    val res = intToByteArray(x)
    adjust(value = res, originalSize = 4, goalSize = size, signExtension = true)
  }

  // short
  def shortToByteArray(x: Short) = ByteBuffer.allocate(2).putShort(x).array()
  def shortToByteArray(x: Short, size: Int) : Array[Byte] = {
    val res = shortToByteArray(x)
    adjust(value = res, originalSize = 2, goalSize = size, signExtension = true)
  }

  // long
  def longToByteArray(x: Long) = ByteBuffer.allocate(8).putLong(x).array()
  def longToByteArray(x: Long, size: Int) : Array[Byte] = {
    val res = longToByteArray(x)
    adjust(value = res, originalSize = 8, goalSize = size, signExtension = true)
  }

  // byte
  def byteToByteArray(x: Byte) = ByteBuffer.allocate(1).put(x).array()
  def byteToByteArray(x: Byte, size: Int) : Array[Byte] = {
    val res = byteToByteArray(x)
    adjust(value = res, originalSize = 1, goalSize = size, signExtension = true)
  }

  // double
  def doubleToByteArray(x: Double) = {
    val l = java.lang.Double.doubleToLongBits(x)
    longToByteArray(l)
  }

  def doubleToByteArray(x: Double, size:Int = 8) = {
    if (size < 8) throw new Exception(s"Double data should be at least 8 bytes, but given ${size}")
    val l = java.lang.Double.doubleToLongBits(x)
    adjust(longToByteArray(l), originalSize = 8, goalSize = size)
  }

  // float
  def floatToByteArray(x: Float) = {
    val l = java.lang.Float.floatToIntBits(x)
    intToByteArray(l)
  }
  def floatToByteArray(x: Float, size:Int = 4) = {
    if (size < 4) throw new RuntimeException(s"Float data should be at least 4 bytes, but given ${size}")
    val l = java.lang.Float.floatToIntBits(x)
    adjust(intToByteArray(l), originalSize = 4, goalSize = size)
  }

  /**
   * -1 --> 255
   * @param x
   * @return
   */
  def byteToUnsigned(x:Byte) :Int = {
    (0xFF & x).toInt
  }

  def unsignedToByte(x:Int) :Byte = {
    assert(x >= 0 && x < 256)
    if (x >= 0 && x < 256/2) x.toByte
    else (x - 256).toByte
  }

  // string
  def stringToByteArray(x: String, n:Int = -1) = {
    // we implement Pascal type string (that has size of string as the first element)
    val size = if (n == -1) (x.size + 1) else n
    assert (size >= (x.size + 1), s"${size} >= ${x.size + 1}???")
    val diff = size - (x.size + 1)
    Array[Byte](unsignedToByte(x.size)) ++ x.getBytes() ++ new Array[Byte](diff)
  }

  /**
   * detect the location of 0
   * http://stackoverflow.com/questions/23976309/trimming-byte-array-when-converting-byte-array-to-string-in-java-scala
   * @param x
   */
  def byteArrayToString(x: Array[Byte]) = {
    val size = byteToUnsigned(x(0))
    if(x.size < size + 1)
      throw new RuntimeException(s"x.size(${x.size}}) is smaller than (size+1)(${size+1}) ")
    new String(x.slice(1, size + 1), "ASCII")
  }

  def byteArrayToStringNoInterpret(x: Array[Byte]) = {
    new String(x, "ASCII")
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
      adjust(byteArray, originalSize = byteArray.size, goalSize = goalSize)
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

