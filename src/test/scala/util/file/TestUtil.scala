package util.file

import org.scalatest._
import Util._

/**
  * You should execute runme.sh in `./src/test/resources/util/file/` directory to create the class files.
  * to create the class files in the modules directory.
  */

class TestUtil extends FunSuite {
  test ("getFileNames test") {
    // http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    val namespace = "modules"
    val directory = "./src/test/resources/util/file/"
    val files = getFileNames(directory, namespace)

    assert("modules.A:modules.B:modules.C" == files.mkString(":"))
  }

  test ("getClassInstances test") {
    // http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    val namespace = "modules"
    val directory = "./src/test/resources/util/file/modules/"
    val instantiations = getClassInstances[chitchat.types.Bit](directory, namespace)

//    assert("modules.A:modules.B:modules.C" == files.mkString(":"))
  }
}
