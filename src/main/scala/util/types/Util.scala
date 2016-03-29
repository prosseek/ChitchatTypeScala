package util.types

import chitchat.types._
import util.file.Util._
/**
  * Created by smcho on 3/28/16.
  */
object Util {

  def getSystemTypeInstances() = {
    List[chitchat.types.Bit](
      new Byte,
      new UByte,
      new Date
    )
  }

  def getUserTypeInstances(directory:String, namespace:String = "chitchat.types") = {
    // val directory = "./src/test/resources/util/file/"
    getClassInstances[chitchat.types.Bit](directory, namespace)
  }

  def getTypeInstances(directory:String) = {
    getSystemTypeInstances() ++ getUserTypeInstances(directory)
  }
}
