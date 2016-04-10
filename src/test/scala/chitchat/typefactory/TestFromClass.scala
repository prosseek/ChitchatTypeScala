package chitchat.typefactory

import org.scalatest.FunSuite

class TestFromClass extends FunSuite {
  test ("getFileNames test") {
    val expectRange = Set[String]("byte", "ubyte", "boolean", "age", "level")
    val expectEncode = Set[String]("date", "time", "latitude", "longitude")
    val expectEtc = Set[String]("string", "float", "name")

    val result = FromClass.getSystemTypeInstances.keys.toSet
    assert(expectRange ++ expectEncode ++ expectEtc == result)
  }

  test ("getSystemTypeInstances test") {
    val types = FromClass.getSystemTypeInstances
    assert(types("string").asInstanceOf[chitchat.types.String].name == "string")
    assert(types("ubyte").asInstanceOf[chitchat.types.range.UByte].size == 8)
  }

  test ("get user type instances") {
    val userMap = FromClass.getUserTypeInstances(directory = "./src/test/resources/util/file/")

    userMap.foreach {
      case (key, value) =>
        key match {
          // from the name we know that the type inherits from Rnage so we can use the asInstanceOf
          case "a" => assert(value.asInstanceOf[chitchat.types.Range].size == 5)
          case "b" => assert(value.asInstanceOf[chitchat.types.Range].size == 10)
          case "c" => assert(value.asInstanceOf[chitchat.types.Range].size == 15)
          case "d" => assert(value.asInstanceOf[chitchat.types.Encoding].size == 16)
          case "e" => assert(value.asInstanceOf[chitchat.types.Float].name == "e")
          case "f" => assert(value.asInstanceOf[chitchat.types.String].name == "f")
          case _ => throw new RuntimeException(s"Something's wrong ${key}")
        }
    }
  }
}
