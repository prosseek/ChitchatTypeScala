package util.types

import org.scalatest.FunSuite
import Util._

class TestUtil extends FunSuite {
  test ("getFileNames test") {
    val expectRange = Set[String]("byte", "ubyte", "boolean", "age", "level")
    val expectEncode = Set[String]("date", "time", "latitude", "longitude")
    val expectEtc = Set[String]("string", "float")

    val result = getSystemTypeInstances.keys.toSet
    assert(expectRange ++ expectEncode ++ expectEtc == result)
  }

  test ("getSystemTypeInstances test") {
    val types = getSystemTypeInstances
    assert(types("string").asInstanceOf[chitchat.types.String].name == "string")
    assert(types("ubyte").asInstanceOf[chitchat.types.range.UByte].size == 8)
  }

  test ("get user type instances") {
    val userMap = getUserTypeInstances(directory = "./src/test/resources/util/file/")

    userMap.foreach {
      case (key, value) =>
        key match {
          // from the name we know that the type inherits from Rnage so we can use the asInstanceOf
          case "range_a" => assert(value.asInstanceOf[chitchat.types.Range].size == 5)
          case "range_b" => assert(value.asInstanceOf[chitchat.types.Range].size == 10)
          case "range_c" => assert(value.asInstanceOf[chitchat.types.Range].size == 15)
          case "encoding_d" => assert(value.asInstanceOf[chitchat.types.Encoding].size == 16)
          case "float_e" => assert(value.asInstanceOf[chitchat.types.Float].name == "float_e")
          case "string_f" => assert(value.asInstanceOf[chitchat.types.String].name == "string_f")
        }
    }
  }
}
