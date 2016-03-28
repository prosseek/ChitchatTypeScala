package util.conversion

/** Provides functions for conversion
  *
  *  1. Conversion functions
  *     1. dms to dd
  *  1. bit width calculation functions
  *     1. getBitsForValue: value 4 requires 3 bits `(000 - 111(7))` to represent it
  *     1. getBytesForBits: 6 bits requires 2 bytes
  */

object Util {

  /********************************************************/
  // dms to dd
  /********************************************************/

  /**
    * Location inforamtion format change. DMS (Day, Month, Second1, Second2) format is modified into floating point DD format
    *
    * ==== Why ====
    * For geolocation information representation there are dms and dd format.
    *      These functions does the conversion.
    *
    * ==== Notice ====
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

  /********************************************************/
  // getBits and getBytes functions
  /********************************************************/

  /**
    * Given value, returns the total number of bits to represent the value
    *
    * {{{
    *   3 -> we need two bits
    *     3 bits can represent from 0 to (2**2-1)
    *   100 -> we need 7 bits
    *     7 bits can represent from 0 to (2**7 - 1)
    * }}}
    *
    * ==== Why ====
    *
    * BF table width are measured in byte size, but the width parameter 'q' is measured in bits.
    *      So, I need to get the values; for example, I have 15 bits, how many bytes are needed?
    *      This group of function includes other conversion; for example, how many bits are needed for a value 10?
    *
    * ==== Warning ====
    *  - the algorithm only checks the value between `2**0` and `2**100`
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
