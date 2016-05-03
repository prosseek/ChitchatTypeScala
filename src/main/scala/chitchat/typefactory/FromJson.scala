package chitchat.typefactory

import java.lang.{String => JString, Float => JFloat}

import chitchat.types.{Encoding, Range, Float, String}

import scala.collection.mutable.ListBuffer
import scala.{Int => SInt, Char => SChar}

/* This is a schema of a type plugin
  val json1 =

 */

object FromJson {

  def getType(m: Map[JString, Any]) = {
    if (m("plugin").asInstanceOf[JString] != "type")
      throw new RuntimeException(s"The JSON input is not a type plugin")
    m("type").asInstanceOf[JString] match {
      case "encoding" => getEncoding(m)
      case "range" => getRange(m)
      case "float" => getFloat(m)
      case "string" => getString(m)
    }
  }

  /**
    * ==== Encoding Example ====
    * {{{
      {
        "plugin" : "type",
        "type" : "encoding",
        "name" : "hello",
        "count" : 2,
        "range1_name" : "a",
        "range1_size" : 5,
        "range1_signed" : 0,
        "range1_min" : 0,
        "range1_max" : 10,
        "range2_name" : "b",
        "range2_size" : 5,
        "range2_signed" : 1,
        "range2_min" : -10,
        "range2_max" : 10,
      }
    * }}}
    *
    * ==== Type definition ====
    * {{{
    *   class Encoding(override val name:java.lang.String, val elements:Seq[Range])
    *   extends Base[Seq[scala.Int]](name = name) with Checker
    * }}}
    * @param m
    * @return
    */
  def getEncoding(m: Map[JString, Any]) = {
    val typeName = m("type").asInstanceOf[JString]
    if (typeName != "encoding")
      throw new RuntimeException(s"not encoding type - ${typeName}")

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

    new Encoding(name = name , elements = ranges)
  }

  /**
    * ==== Type definition ====
    * {{{
    *   class Range(override val name:java.lang.String = "",
            var size:Int = 0,
            val signed:scala.Boolean = false,
            val min:Int = 0,
            val max:Int = 0
            )
    * }}}
    *
    * ==== example ====
    * {{{
      {
        "plugin" : "type",
        "type" : "range",
        "name" : "hello",
        "range_size" : 5,
        "range_signed" : 0,
        "range_min" : 0,
        "range_max" : 10
      }
    * }}}
    * @param m
    * @return
    */
  def getRange(m: Map[JString, Any]) = {
    val name = m("name").asInstanceOf[JString]

    val size = m(s"range_size").asInstanceOf[SInt]
    val signed = if (m(s"range_signed").asInstanceOf[SInt] == 1) true else false
    val min = m(s"range_min").asInstanceOf[SInt]
    val max = m(s"range_max").asInstanceOf[SInt]

    new Range(name = name , size = size, signed = signed, min = min, max = max)
  }

  /**
    * ==== type definition ====
    * {{{
       class Float(override val name: JString = "float",
            val ranges:Seq[JFloat] = null,
            val shift:JFloat = 0.0f
            )
    * }}}
    *
    * ==== example ====
    * {{{
      {
        "plugin" : "type",
        "type" : "float",
        "name" : "hello",
        "range_min" : -10.0,
        "range_max" : 10.0,
        "shift" : 1.0
      }
    *
    * }}}
    * @param m
    * @return
    */
  def getFloat(m: Map[JString, Any]) = {
    val name = m("name").asInstanceOf[JString]

    val min = m(s"range_min").asInstanceOf[JFloat]
    val max = m(s"range_max").asInstanceOf[JFloat]
    val shift = m(s"shift").asInstanceOf[JFloat]

    new Float(name = name , min = min, max = max, shift = shift)
  }

  /**
    * ==== type definition ====
    * {{{
       class String(override val name:JString = "string",
             min:scala.Char = 0.toChar,
             max:scala.Char = 0.toChar,
             conditions:Seq[Any] = Seq[Any]())
       extends Base[JString](name) with Checker {
    * }}}
    *
    * ==== example ====
    * {{{
       {
        "plugin" : "type",
        "type" : "string",
        "name" : "hello",
        "range_min" : "a",
        "range_max" : "b",
        "condition" : "maxlength",
        "limit" : 10
       }
    * }}}
    *
    * ==== caveat ====
    *  1. The range_min/max uses string as input.
    *  2. The string is converted into Scala.Char, so only the first char in the string is used.
    *
    * @param m
    * @return
    */
  def getString(m: Map[JString, Any]) = {
    val name = m("name").asInstanceOf[JString]

    val min = m(s"range_min").asInstanceOf[JString]
    val max = m(s"range_max").asInstanceOf[JString]

    // Condition only minlength, maxlength
    // List("maxlength", 10)
    val conditions = ListBuffer[Any](m(s"condition").asInstanceOf[JString], m(s"limit").asInstanceOf[SInt])

    new String(name = name , min = min(0), max = max(0), conditions = conditions.toSeq)
  }
}
