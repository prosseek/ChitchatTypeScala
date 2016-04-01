package chitchat.types

trait Checker {
  def checkRange(totalBytes:scala.Int, byteArray:Array[Byte]) : scala.Boolean = {
    // 1. The byteArray should have enough room to be decoded
    // totalBytes to represent the value is (for example) 10 bytes.
    // However, the byteArray.size is 9 bytes. This means the byteArray cannot be decoded.
    if (totalBytes > byteArray.size)
      return false

    // 2. The excessive bytes should be 0
    val expectedZeroValues = byteArray.slice(totalBytes, byteArray.size)
    expectedZeroValues.foreach { byte =>
      if (!(byte == 0)) return false
    }
    true
  }
}
