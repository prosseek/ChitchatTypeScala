package chitchat.types.encoding

import chitchat.types._

// (8, -90, 90), (6, 0, 59), (6, 0, 59), (7, 0, 99)
class Latitude extends Encoding(
  name = "latitude",
  Array[Range](
    new Range(name = "hour",  size = 8, min = -90, max = 90, signed = true),
    new Range(name = "minute", size = 6, min =   0, max = 59, signed = false),
    new Range(name = "second1",   size = 6, min =   0, max = 59, signed = false),
    new Range(name = "second2",   size = 7, min =   0, max = 99, signed = false)))

