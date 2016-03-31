### Endianness of Chitchat type

ByteArray functions used for integer (Long/Int/Short/Byte) types are using
`java.nio.ByteBuffer` methods.

The default conversion is bigendian where higher bits stored in lower bits of memory.

    scala> import java.nio.ByteBuffer
    scala> val x = 1
    scala> ByteBuffer.allocate(4).putInt(x).array()
    res7: Array[Byte] = Array(0, 0, 0, 1) <-- lower bit of 1 comes last

The reverse works with wrap method.

    scala> ByteBuffer.wrap(res7).getInt
    res8: Int = 1

For String, we use String's getBytes() method, and it maps a string into an array of bytes one by one.

    scala> "12345".getBytes()
    res10: Array[Byte] = Array(49, 50, 51, 52, 53)

The reverse can be implmented with a String object instantiation.

    scala> new String(res10, "ASCII")
    res13: String = 12345

### Level of endianness

We don't use endian at the bit level, we use endianness only at byte level.

    12:34 -> the value 12 and 34 is big endian.

### Policy change [2016/03/31]04:27PM
I don't use endianness, always use big endian format.
I have adjust function that understands the endianness, but it's for making the function as general as possible.
