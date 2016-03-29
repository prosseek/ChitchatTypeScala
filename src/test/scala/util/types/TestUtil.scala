package util.types

import org.scalatest._
import Util._

class TestUtil extends FunSuite {
  test ("getFileNames test") {
    val expect = Set[String]("byte", "ubyte", "date")

    val result = getSystemTypeInstances().map(_.name).toSet
    assert(expect == result)
  }

  test ("get user type instances") {
    getUserTypeInstances(directory = "./src/test/resources/util/file/").foreach {ins =>
      ins.name match {
        case "A" => assert(ins.size == 100)
        case "B" => assert(ins.size == 200)
        case "C" => assert(ins.size == 300)
      }
    }
  }
}
