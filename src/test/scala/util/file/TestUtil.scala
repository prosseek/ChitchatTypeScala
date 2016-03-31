// TODO: Check this out. Can we remove (or do we have to remove) the package in test files? Is this IntellJ's bug
//package util.file
/*
   I commented out the package because I sometimes have error message, but I don't know how to resolve this issue.

   {{{
    type Bit is not a member of package util.file.chitchat.types
    [error]     val instantiations = getClassInstances[chitchat.types.Bit](directory, namespace)
   }}}

   I guess this is IntelliJ's bug.
 */

import org.scalatest._
import chitchat.types._

import util.file.Util._

class TestUtil extends FunSuite {

  test ("getFileNames test") {
    // http://stackoverflow.com/questions/5285898/how-to-access-test-resources
    // val currentDirectory = (new File(".")).getAbsoluteFile()
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
    val directory = "./src/test/resources/util/file/"
    val instantiations = getClassInstances[chitchat.types.Base[_]](directory, namespace)

    instantiations.foreach { instance =>
      instance.name match {
        case "range_A" => assert(instance.asInstanceOf[Range].size == 100)
        case "range_B" => assert(instance.asInstanceOf[Range].size == 200)
        case "range_C" => assert(instance.asInstanceOf[Range].size == 300)
      }
    }
  }
}
