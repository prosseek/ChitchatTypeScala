### [2016/03/31]07:12PM

size is measured in bits.
sizeInBytes is measured in bytes.

### [2016/03/31]04:22PM

For Range based type, the maximum length is 4 bytes (unsigned).

### [2016/03/31]04:17PM

When the byte array that represents the value is larger than the size of type, it means the additional bits are added in higher bytes.

    UByte => 1 byte (V)
    byte array => 6 bytes (bytes 1 - 5 should be zero)

    012345 <- index in the byte array
    ------
    V00000
    *

### [2016/03/31]03:02PM

User defined type's name should indicate if the type is range type or encoded type from its name.

    * range_XYZ // this name indicates the type is in Bit
    * encoded_ZYZ // this name indicates the type is in Encoded
