package chitchat.types.encoding

import chitchat.types._

class Date extends Encoding(
  name = "date",
  Array[Range](
    new Range(name = "year",  size = 7, min = -64, max = 63, signed = true),
    new Range(name = "month", size = 4, min =   1, max = 12, signed = false),
    new Range(name = "day",   size = 5, min =   1, max = 31, signed = false)))

