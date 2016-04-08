package chitchat.typefactory

import java.lang.{String => JString}

import chitchat.types.{Encoding, Range}

import scala.collection.mutable.ListBuffer
import scala.{Int => SInt}

object FromJson {

  def getEncoding(m: Map[JString, Any]) = {
    val count = m("count").asInstanceOf[SInt]
    val name = m("name").asInstanceOf[JString]

    val ranges = ListBuffer[Range]()

    // retrieve all the information
    for (i <- 1 to count) {
      val name = m(s"range${i}_name").asInstanceOf[JString]
      val size = m(s"range${i}_size").asInstanceOf[SInt]
      val signed = if (m(s"range${i}_signed").asInstanceOf[SInt] == 1) true else false
      val min = m(s"range${i}_min").asInstanceOf[SInt]
      val max = m(s"range${i}_max").asInstanceOf[SInt]
      ranges += new Range(name = name, size = size, signed = signed, min = min, max = max)
    }

    new Encoding(name = name , ranges)
  }

  def getRange(m: Map[JString, Any]) = {
    val count = m("count").asInstanceOf[SInt]
    val name = m("name").asInstanceOf[JString]

    val size = m(s"range_size").asInstanceOf[SInt]
    val signed = if (m(s"range_signed").asInstanceOf[SInt] == 1) true else false
    val min = m(s"range_min").asInstanceOf[SInt]
    val max = m(s"range_max").asInstanceOf[SInt]

    new Range(name = name , size = size, signed = signed, min = min, max = max)
  }
}
