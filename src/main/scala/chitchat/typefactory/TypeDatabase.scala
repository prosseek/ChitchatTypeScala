package chitchat.typefactory

import java.lang.{Float => JFloat, String => JString}

import collection.mutable.{ArrayBuffer, Map => MMap}
import scala.{Byte => SByte, Int => SInt}
import chitchat.types._

// [2016/04/08]01:54PM
// todo: What if the system name is overriden from user's library? add check code.
// todo: What if the user's library name is overriden from type from json?

object TypeDatabase {
  def apply(directories:Seq[JString] = null) = {
    val tb = new TypeDatabase

    if (directories != null) {
      tb.create(directories)
    }
    else
      tb.create
    tb
  }
}

/**
  * TypeDatabase is a wrapper of a dictionary (name (String type) -> Type (Base type))
  */
class TypeDatabase {
  private val mmap = MMap[JString, chitchat.types.Base[_]]()

  def create : Unit = {
    mmap.clear()
    mmap ++= FromClass.getSystemTypeInstances
  }

  def create(directories:Seq[JString]) : Unit = {
    create
    directories foreach {
      directory => addToDatabase(directory)
    }
  }

  def addToDatabase(directory:JString) = {
    mmap ++= FromClass.getUserTypeInstances(directory)
  }

  // todo: Add to database from JSON
  def add() = {}
  // todo: implement update/remove
  def update() = ???
  def delete() = ???

  def get(label:JString) : Option[Base[_]] = {
    mmap.get(label)
  }

  def encode(label:JString, value:Any) : Option[Array[Byte]] = {
    val instance = mmap.get(label)
    if (instance.isEmpty) {
      // if the value is of string type, we can just encode them as string
      if (value.isInstanceOf[JString]) {
        val v = mmap.get("string").get
        v.asInstanceOf[String].encode(value.asInstanceOf[JString])
      }
      else if (value.isInstanceOf[JFloat]) {
        val v = mmap.get("float").get
        v.asInstanceOf[Float].encode(value.asInstanceOf[JFloat])
      }
      else if (value.isInstanceOf[Int]) {
        val v = mmap.get("short").get
        v.asInstanceOf[range.Short].encode(value.asInstanceOf[Int])
      }
      else
        throw new RuntimeException(s"No instance for ${label}")
    }
    else {
      instance match {
        case Some(v:Encoding) => v.encode(value.asInstanceOf[Seq[Int]])
        case Some(v:Range) => v.encode(value.asInstanceOf[Int])
        case Some(v:Float) => v.encode(value.asInstanceOf[JFloat])
        case Some(v:String) => v.encode(value.asInstanceOf[JString])
        case Some(_) | None => None
      }
    }
  }

  /** Returns value from byte array
    *
    * ==== Example ====
    * {{{
    *   "float", Instance_of_float, -4:2:45:11 => 12.34f
    *   "string", 5Hello, 5:H:e:l:l:o => "Hello"
    * }}}
    *
    * @param label
    * @param ba
    * @return
    */
  def decode(label:JString, ba:Array[Byte]) : Option[Any] = {
    val instance = get(label)
    if (instance.isEmpty) {
      None
    }
    else {
      instance match {
        case Some(v:Float) => Some(util.conversion.ByteArrayTool.byteArrayToFloat(ba))
        case Some(v:String) => Some(util.conversion.ByteArrayTool.byteArrayToString(ba))
        case Some(v:Encoding) => v.decode(ba)
        case Some(v:Range) => v.decode(ba)
        case Some(_) | None => throw new RuntimeException(s"Unknown type ${instance.get.name}")
      }
    }
  }
}
