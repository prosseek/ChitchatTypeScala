package chitchat.types.string

import java.lang.{String => JString}

/**
  * Name is a string with a constraint that the length should be smaller than 15
  */
class Name extends chitchat.types.String (name = "name") {
  override def check(value: JString): scala.Boolean = {
    if (!super.check(value)) return false
    else {
      (value.length <= 15)
    }
  }
}
