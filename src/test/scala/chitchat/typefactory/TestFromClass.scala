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
    // todo: removed object (class) type plugins, test with summary type plugins

  }
}
