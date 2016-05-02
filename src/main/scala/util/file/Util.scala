package util.file

import scala.collection.mutable._
import java.io.File

/**
  * Provides functions for directory/files
  */

object Util {
// todo: the plugin now uses summary (binary or textual (JSON))

  /**
    * Returns the class names in a directory prepended with namespace
    *
    * ==== Example ====
    * {{{
    *   The base directory is A/B.
    *   The class has namespace a.b.
    *   The A/B/modules directory has A.class, B.class and C.class
    *
    *   getFileNames("A/B/", "a.b")
    *   ->
    *   List[String]("a.b.A, a.b.B, a.b.C")
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

//  def getFileNames(directory:String, namespace:String) = {
//    val files = ArrayBuffer[String]()
//    val namespaceString = namespace.replace(".", "/")
//    val d = new File(directory + "/" + namespaceString)
//    if (d.exists && d.isDirectory) {
//      d.listFiles.filter(_.getName.endsWith(".class"))
//        .map(namespace + "." + _.getName.replace(directory, "").replace(namespaceString, "")
//          .replace(".class","")).toList
//    } else {
//      List[String]()
//    }
//  }

  /**
    * Returns an instantiation of classes in the directory.
    *
    * ==== Example ====
    * {{{
    *   A/B/modules directory has A.class, B.class and C.class
    *   getClassInstances[modules.A]("A/B/", "modules")
    *   ->
    *   List[modules.A](instance of A, instance of B, instance of C)
    * }}}
    *
    * @param directory
    * @param namespace
    * @tparam T
    * @return
    */

//  def getClassInstances[T](directory:String, namespace:String) = {
//    val files = getFileNames(directory, namespace)
//
//    files.map(Class.forName(_).newInstance().asInstanceOf[T])
//  }
}