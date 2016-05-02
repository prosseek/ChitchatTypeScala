package chitchat.types.encoding

import chitchat.types._

// (9, -180, 180), (6, 0, 59), (6, 0, 59), (7, 0, 99)
class Longitude extends Encoding(
  name = "longitude",
  Array[Range](
    new Range(name = "hour",  size = 9, min = -180, max = 180, signed = true),
    new Range(name = "minute", size = 6, min =   0, max = 59, signed = false),
    new Range(name = "second1",   size = 6, min =   0, max = 59, signed = false),
    new Range(name = "second2",   size = 7, min =   0, max = 99, signed = false))
)

