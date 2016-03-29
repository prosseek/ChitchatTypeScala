package chitchat.types

/**
  * Created by smcho on 3/28/16.
  */
class Date extends Encoding(
  name = "date",
  Array[Bit](
    new Bit(name = "year",  size = 7, min = -64, max = 63, signed = true),
    new Bit(name = "month", size = 4, min =   1, max = 12, signed = false),
    new Bit(name = "day",   size = 5, min =   1, max = 31, signed = false)))
{

}
