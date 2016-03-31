package chitchat.types.encoding

import chitchat.types._

class Time extends Encoding(
  name = "time",
  Array[Bit](
    new Bit(name = "hour",     size = 5, min = 0, max = 24, signed = false),
    new Bit(name = "minute",   size = 6, min = 0, max = 59, signed = false)))
