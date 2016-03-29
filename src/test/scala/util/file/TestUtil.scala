package util.file

import org.scalatest._
//import java.io.File

import Util._

class TestUtil extends FunSuite {
  test ("getFileNames test") {
    // http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    //val currentDirectory = (new File(".")).getAbsoluteFile()
    val namespace = "chitchat.types"
    //val directory = currentDirectory + "/src/test/resources/util/file/"
    val directory = "./src/test/resources/util/file/"
    val files = getFileNames(directory, namespace)

    assert("chitchat.types.A:chitchat.types.B:chitchat.types.C" == files.mkString(":"))
  }

  /**
    * ==== Preparation for this test ====
    *
    * 1. Run `sbt package` to create the jar file that contains the Bit class
    * 2. `test/resources/file` directory has three files that implements Bit class,
    *     You should first execute the `runme.sh` to get class files in `chitchat.types` sub-directory.
    */
  test ("getClassInstances test") {
    // http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    //val currentDirectory = (new File(".")).getAbsoluteFile()
    val namespace = "chitchat.types"
    //val directory = currentDirectory + "./src/test/resources/util/file/"
    val directory = "./src/test/resources/util/file/"
    val instantiations = getClassInstances[chitchat.types.Bit](directory, namespace)

    instantiations.foreach { instance =>
      instance.name match {
        case "A" => assert(instance.size == 100)
        case "B" => assert(instance.size == 200)
        case "C" => assert(instance.size == 300)
      }
    }
  }
}
