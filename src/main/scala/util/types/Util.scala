package util.types

import chitchat.types.range._
import chitchat.types.encoding._
import util.file.Util._

import java.lang.{String => JString}

object Util {

  def getSystemTypeInstances() = {
    List[chitchat.types.Base[_]](
      new Byte,
      new UByte,
      new Date
    )
  }

  def getUserTypeInstances(directory: JString, namespace: JString = "chitchat.types") = {
    // val directory = "./src/test/resources/util/file/"
    getClassInstances[chitchat.types.Bit](directory, namespace)
  }

  def getTypeInstances(directory:JString) = {
    getSystemTypeInstances() ++ getUserTypeInstances(directory)
  }
}
