package chitchat.types.encoding

import chitchat.types._

class Time extends Encoding(
  name = "time",
  Array[Range](
    new Range(name = "hour",     size = 5, min = 0, max = 24, signed = false),
    new Range(name = "minute",   size = 6, min = 0, max = 59, signed = false)))
