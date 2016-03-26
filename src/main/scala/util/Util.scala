package util

/**
  Utility module contains functions that are used by other modules.

  1. Conversion functions
  1.1. byte to/from unsigned
  1.2. dms to dd

  2. bit width calculation functions
  2.1 getBitsForValue: value 4 requires 3 bits (000 - 111(7)) to represent it
  2.2 getBytesForBits: 6 bits requires 2 bytes

  */

object Util {

  /******************************************************************
    * byte to/from unsigned functions
    ******************************************************************
    * Why: Scala does not support unsigned type, but we need a byte that contains value from 0x00 to 0xFF without interpreting
    * the value with highest bit set as negative value.
    *
    * How: From a byte type value, we create an int type value that ranges from 0x00 to 0xFF from masking with 0xFF.
    */

  /**
    * Given signed byte, returns a int type range from 0 - 255.
    * example:
    *  +1 --> 1 (4 byte)
    *  -1 --> 255 (4 byte)
    *
    * @param x
    * @return Int type value range from 0x00 to 0xFF
    */
  def byteToUnsigned(x:Byte) = {
    // 0xFF & operation gives the int value, so we don't need the toInt
    (0xFF & x)
  }

  def unsignedToByte(x:Int) = {
//    if (x < 0 || x >= 256) throw new RuntimeException(s"Input value is out of range ${x}")
//    if (x >= 0 && x < 256/2) x.toByte
//    else (x - 256).toByte
    (x & 0xFF).toByte
  }

  /******************************************************************
    * dms to dd
    ******************************************************************
    *
    * Why: For geolocation information representation there are dms and dd format.
    *      These functions does the conversion.
    */

  /**
    * Location inforamtion format change
    * DMS (Day, Month, Second1, Second2) format is modified into floating point DD format
    *
    * Only Tuple or four parameters are used for input
    *
    * @param value
    * @return
    */
  def dms2dd(value:(Int, Int, Int, Int)) :Double = {
    dms2dd(value._1, value._2, value._3, value._4)
  }
  def dms2dd(d:Int, m:Int, s1:Int, s2:Int) = {
    val s:Double = s"${s1}.${s2}".toDouble
    if (d > 0)
      d.toDouble + m.toDouble/60.0 + s/3600.0
    else
      -(-d.toDouble + m.toDouble/60.0 + s/3600.0)
  }

  /******************************************************************
    * bit width functions
    ******************************************************************
    *
    * Why: BF table width are measured in byte size, but the width parameter 'q' is measured in bits.
    *      So, I need to get the values; for example, I have 15 bits, how many bytes are needed?
    *      This group of function includes other conversion; for example, how many bits are needed for a value 10?
    */

  /**
    * Given value, returns the total number of bits to represent the value
    * example:
    *   3 -> we need two bits 0 - (2**2-1) to represent the 3
    *   100 -> we need 7 bits 0 - (2**7 - 1) to represent the value 100
    *
    * Warning: this algorithm checks the value between 2**0 and 2**100
    *
    * @param value
    * @return
    */
  def getBitsForValue(value:Int) : Int = {
    if (value < 0) throw new RuntimeException(s"Input value (${value}) should be positive")
    if (value == 0 || value == 1) return 1
    else
      (0 to 100).foreach { i =>
        if (scala.math.pow(2.0, i) < value && scala.math.pow(2.0, (i+1)) >= value)
          return i+1
      }
    throw new RuntimeException(s"${value} is not between 2^0 and 2^100")
  }

  /**
    * Given bits, returns the byte size to store the bits
    * example:
    *     10 bit -> we need 2 bytes
    *
    * @param bits
    * @return
    */
  def getBytesForBits(bits:Int) = {
    if (bits == 0) 1
    else if (bits % 8 == 0) bits / 8
    else (bits / 8) + 1
  }
}
