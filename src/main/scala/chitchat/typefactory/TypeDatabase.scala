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
    if (instance.isEmpty)
      None
    else {
      instance.get match {
        case v:Encoding => v.encode(v.asInstanceOf[Seq[Int]])
        case v:Range => v.encode(v.asInstanceOf[Int])
        case v:Float => v.encode(v.asInstanceOf[JFloat])
        case v:String => v.encode(v.asInstanceOf[JString])
        case _ => None
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
    val instance = mmap.get(label)
    if (instance.isEmpty)
      None
    else {
      instance.get match {
        case v:Float => {
          val result = util.conversion.ByteArrayTool.byteArrayToFloat(ba)
          Some(result)
        }
        case v:String => {
          val result = util.conversion.ByteArrayTool.byteArrayToString(ba)
          Some(result)
        }
        case v:Encoding => {
          val encodingInstance = instance.asInstanceOf[Encoding]
          encodingInstance.decode(ba)
        }
        case v:Range => {
          val rangeInstance = instance.asInstanceOf[Range]
          rangeInstance.decode(ba)
        }
        case _ => {
          throw new RuntimeException(s"Unknown type ${instance.get.name}")
        }
      }
    }
  }
}