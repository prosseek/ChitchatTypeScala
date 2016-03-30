package chitchat.types.encoding

import org.scalatest._

/**
  * Created by smcho on 3/28/16.
  */
class TestDate extends FunSuite {

  test ("date test") {
    val date = new Date
    assert(date.name == "date")
    assert(date.size == 16)
  }

}
