package chitchat.types

class D extends Encoding(
  name = "d",
  Array[Range](
    new Range(name = "y", size = 7, min = -64, max = 63, signed = true),
    new Range(name = "m", size = 4, min =   1, max = 12, signed = false),
    new Range(name = "d", size = 5, min =   1, max = 31, signed = false)))