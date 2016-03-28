package util.file

import scala.collection.mutable._
import java.io.File

/**
  * Provides functions for directory/files
  */

object Util {

  /**
    * Returns the class names in a directory prepended with namespace
    *
    * ==== Example ====
    * {{{
    *   A/B/modules directory has A.class, B.class and C.class
    *   getFiles("A/B/modules", "modules")
    *   ->
    *   List[String]("modules.A, modules.B, modules.C")
    * }}}
    *
    * ==== Why ====
    *
    * `Class.forName(className)` returns a class object, and `newInstance()` method will create
    * an instance of the `className` type in Java.
    *
    * {{{
    *   val a = (Class.forName("modules.C").newInstance()).asInstanceOf[modules.A]
    *   a.hello()
    * }}}
    *
    * This function can be used to create the instances of the classes in a directory.
    *
    * @param directory
    * @param namespace
    * @return List[String] of the class names in the directory (given namespace prependes)
    */

  private def getFiles(directory:String, namespace:String) = {
    val files = ArrayBuffer[String]()
    val d = new File(directory)
    if (d.exists && d.isDirectory) {
      // http://stackoverflow.com/questions/36254248/sequencing-collections-in-scala/
      d.listFiles.filter(_.getName.endsWith(".class"))
        .map(namespace + "." + _.getName.replace(directory, "")
          .replace(".class","")).toList
    } else {
      List[String]()
    }
  }

  /**
    * Returns an instantiation of classes in the directory.
    *
    * ==== Example ====
    * {{{
    *   A/B/modules directory has A.class, B.class and C.class
    *   getClasses[modules.A]("A/B/modules", "modules")
    *   ->
    *   List[modules.A](instance of A, instance of B, instance of C)
    * }}}
    *
    * @param directory
    * @param namespace
    * @tparam T
    * @return
    */

  def getClasses[T](directory:String, namespace:String) = {
    val files = getFiles(directory, namespace)

    files.map(Class.forName(_).newInstance().asInstanceOf[T]).toList
  }
}