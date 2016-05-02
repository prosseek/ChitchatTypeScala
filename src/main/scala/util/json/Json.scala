/*
 * Copyright 2009 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// https://raw.githubusercontent.com/stevej/scala-json/master/src/main/scala/com/twitter/json/Json.scala
package util.json

import java.io.PrintWriter
import java.nio.file.{Files, Paths}

import scala.io.Source
import scala.util.Sorting
import scala.util.parsing.combinator._

trait JsonSerializable {
  def toJson(): String
}

/**
  * An Exception thrown when parsing or building JSON.
  */
class JsonException(reason: String) extends Exception(reason)


private class EscapedStringParser extends JavaTokenParsers {
  override protected val whiteSpace = "".r

  def unicode: Parser[String] = rep1("\\u" ~> """[a-fA-F0-9]{4}""".r) ^^ { stringBytes =>
    new String(stringBytes.map(Integer.valueOf(_, 16).intValue.asInstanceOf[Char]).toArray)
  }

  def escaped: Parser[String] = "\\" ~> """[\\/bfnrt"]""".r ^^ { charStr =>
    val char = charStr match {
      case "r" => '\r'
      case "n" => '\n'
      case "t" => '\t'
      case "b" => '\b'
      case "f" => '\f'
      case x => x.charAt(0)
    }
    char.toString
  }

  def characters: Parser[String] = """[^\"[\x00-\x1F]\\]+""".r // comment to fix emac parsing "

  def string: Parser[String] = "\"" ~> rep(unicode | escaped | characters) <~ "\"" ^^ { list =>
    list.mkString("")
  }

  def parse(s: String) = {
    parseAll(string, s) match {
      case Success(result, _) => result
      case x @ Failure(msg, z) => throw new JsonException(x.toString)
      case x @ Error(msg, _) => throw new JsonException(x.toString)
    }
  }
}

/**
  * Stolen (awesomely) from the scala book and fixed by making string quotation explicit.
  */
private class JsonParser extends JavaTokenParsers {
  def obj: Parser[Map[String, Any]] = "{" ~> repsep(member, ",") <~ "}" ^^ (Map.empty ++ _)

  def arr: Parser[List[Any]] = "[" ~> repsep(value, ",") <~ "]"

  def member: Parser[(String, Any)] = string ~ ":" ~ value ^^ {
    case name ~ ":" ~ value => (name, value)
  }

  def number: Parser[Any] = floatingPointNumber ^^ {
    case num if num.matches(".*[.eE].*") => num.toFloat // BigDecimal(num)
    case num => {
      val rv = num.toLong
      if (rv >= Int.MinValue && rv <= Int.MaxValue) rv.toInt else rv
    }
  }

  lazy val stringParser = (new EscapedStringParser)

  def string: Parser[String] = "\"(\\\\\\\\|\\\\\"|[^\"])*+\"".r ^^ { escapedStr =>
    stringParser.parse(escapedStr)
  }

  def value: Parser[Any] = obj | arr | string | number |
    "null" ^^ (x => null) | "true" ^^ (x => true) | "false" ^^ (x => false)

  def parse(s: String) = {
    parseAll(value, s) match {
      case Success(result, _) => result
      case x @ Failure(msg, z) => throw new JsonException(x.toString)
      case x @ Error(msg, _) => throw new JsonException(x.toString)
    }
  }
}


/**
  * An explanation of Scala types and their JSON representations.
  *
  * Natively supported scalar types are: Boolean, Int, Long, String.
  * Collections are Sequence[T], Map[String, T] where T includes the scalars defined above, or
  * recursive Sequence or Map. You are in flavor country.
  */
object Json {
  private[json] def quotedChar(codePoint: Int) = {
    codePoint match {
      case c if c > 0xffff =>
        val chars = Character.toChars(c)
        "\\u%04x\\u%04x".format(chars(0).toInt, chars(1).toInt)
      case c if c > 0x7e => "\\u%04x".format(c.toInt)
      case c => c.toChar
    }
  }

  /**
    * Quote a string according to "JSON rules".
    */
  def quote(s: String) = {
    val charCount = s.codePointCount(0, s.length)
    "\"" + 0.to(charCount - 1).map { idx =>
      s.codePointAt(s.offsetByCodePoints(0, idx)) match {
        case 0x0d => "\\r"
        case 0x0a => "\\n"
        case 0x09 => "\\t"
        case 0x22 => "\\\""
        case 0x5c => "\\\\"
        case 0x2f => "\\/"     // to avoid sending "</"
        case c => quotedChar(c)
      }
    }.mkString("") + "\""
  }

  /**
    * Returns a JSON representation of the given object, as a JsonQuoted object.
    */
  def build(obj: Any): JsonQuoted = {
    val rv = obj match {
      case JsonQuoted(body) => body
      case null => "null"
      case x: Boolean => x.toString
      case x: Number => x.toString
      case array: Array[_] => array.map(build(_).body).mkString("[", ",", "]")
      case list: Seq[_] =>
        list.map(build(_).body).mkString("[", ",", "]")
      case map: scala.collection.Map[_, _] =>
        Sorting.stableSort[(Any, Any), String](map.iterator.toList, { case (k, v) => k.toString }).map { case (k, v) =>
          quote(k.toString) + ":" + build(v).body
        }.mkString("{", ",", "}")
      case x: JsonSerializable => x.toJson()
      case x =>
        quote(x.toString)
    }
    JsonQuoted(rv)
  }

  /**
    * Parses a JSON String representation into its native Scala reprsentation.
    * It returns Map (before it returned Any)
    */
  def parse(s: String) = (new JsonParser).parse(s).asInstanceOf[Map[String, Any]]

  /**
    * Load a JSON string from filePath to parse the content into a map
    * smcho added [2016/04/05]07:55PM
    *
    * @param filePath
    * @return
    */
  def loadJson(filePath: String) = {
    Json.parse(loadJsonContent(filePath))
  }

  def loadJsonContent(filePath: String) = {
    val fileExists = Files.exists(Paths.get(filePath))

    if (fileExists) {
      Source.fromFile(filePath).mkString("")
    }
    else {
      throw new RuntimeException(s"No Json file exists ${filePath}")
    }
  }

  def save(filePath:String, map:Map[String, Any]) = {
    val result = build(map).toString
    val file = new PrintWriter(new java.io.File(filePath))
    file.write(result)
    file.close()
  }

  def isSimpleJson(jsonContent:Map[String, Any]) : Boolean = {
    def supportedType(v:Any) : Boolean = {
      v match {
        case v:Int => true
        case v:Double => true
        case v:String => true
        case v:Seq[_] => {
          v foreach { i =>
            i match {
              case i:Int => true
              case _ => return false
            }
          }
          true
        }
        case _ => return false
      }
    }
    jsonContent foreach {
      case (k, v) => {
        if (!supportedType(v)) return false
      }
    }
    true
  }
}

/**
  * Wrapper for the JSON string representation of a data structure. This class exists to
  * allow objects to be converted into JSON, attached to another data structure, and not
  * re-encoded.
  */
case class JsonQuoted(body: String) {
  override def toString = body
}
