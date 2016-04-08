package chitchat.typetool

import java.lang.{Float => JFloat, String => JString}

import chitchat.typefactory.{FromClass, TypeDatabase}
import chitchat.types._

import scala.collection.mutable.{ArrayBuffer, ListBuffer => LBuffer}
import scala.{Byte => SByte, Int => SInt}

object TypeInference {
  def apply(directories:Seq[JString] = null) = {
    val typeDatabase = TypeDatabase(directories)
    new TypeInference(typeDatabase)
  }
}

/**
  * TypeInference helps identify the chitchat type from the label name
  *
  * ==== Idea ====
  *
  * Chitchat type is one of the four groups: Encoding/Range/Float/String
  * TypeInference finds/guesses what group the label belongs to.
  *
  * @param typeDatabase
  */
class TypeInference(val typeDatabase : TypeDatabase) {

  def get(label:JString) = typeDatabase.get(label)

  def encode(label:JString, value:Any) : Option[Array[SByte]] = {
    /// encode from label
    typeDatabase.encode(label, value)
  }

  def decode(label:JString, byteArray:Array[Byte]) : Option[Any] = {
    // decode from label
    val res = typeDatabase.decode(label, byteArray)
    if (res.isDefined) res
    else {
      // if the byteArray is not in the database, check if the byte array can be encoded into
      // one of the data types
      None
    }
  }

  def getTypeFromByteArray(byteArray:Array[Byte]) : Option[Any] = {
    def check(label:JString) :Option[Any] = {
      val decoded = typeDatabase.get(label).get.decode(byteArray)
      if (decoded.isDefined)
        return Some(decoded.get)
      else
        None
    }
    // try string
    check("string")
    check("byte")
    check("float")
  }
}
