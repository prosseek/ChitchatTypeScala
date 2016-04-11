package chitchat.types

/**
  * The Base class is the root class of all types including system provided and user provided
  *
  * @param name
  */
abstract class Base[T](val name:java.lang.String = "", val correlatedLabels:Seq[java.lang.String]) {
  def encode(value:T) : Option[Array[scala.Byte]]
  def decode(byteArray: Array[scala.Byte]) : Option[T]
  def check(value:T) : scala.Boolean
  def size : Int
  def sizeInBytes: Int
  def correlated:Seq[java.lang.String] = {
    if (correlatedLabels == null) List[java.lang.String]()
    else correlatedLabels
  }
}