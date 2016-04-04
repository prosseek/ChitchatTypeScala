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
          case "range_A" => assert(value.asInstanceOf[chitchat.types.Range].size == 100)
          case "range_B" => assert(value.asInstanceOf[chitchat.types.Range].size == 200)
          case "range_C" => assert(value.asInstanceOf[chitchat.types.Range].size == 300)
        }
    }
  }
}
