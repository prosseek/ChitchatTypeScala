package chitchat.typefactory

import chitchat.types.{Encoding, Range, Float, String}
import org.scalatest.FunSuite


class TestFromJson extends FunSuite {

  val jsonEncoding =
    """
      |{
      |  "plugin" : "type",
      |  "type" : "encoding",
      |  "name" : "hello",
      |  "count" : 2,
      |  "range1_name" : "a",
      |  "range1_size" : 5,
      |  "range1_signed" : 0,
      |  "range1_min" : 0,
      |  "range1_max" : 10,
      |  "range2_name" : "b",
      |  "range2_size" : 5,
      |  "range2_signed" : 1,
      |  "range2_min" : -10,
      |  "range2_max" : 10
      |}
    """.stripMargin

  test ("encoding test") {
    val m = util.json.Json.parse(jsonEncoding)
    var d1 = FromJson.getEncoding(m)
    assert(d1.name == "hello")
    assert(d1.check(Seq(0, 0)) == true)
    assert(d1.check(Seq(0, 120)) == false)
    assert(d1.encode(Seq(3, -2)).get.mkString(":") == "3:-61")

    assert(m.get("plugin").get == "type")
    assert(m.get("type").get == "encoding")
    d1 = FromJson.getType(m).asInstanceOf[Encoding]
    assert(d1.name == "hello")
    assert(d1.check(Seq(0, 0)) == true)
    assert(d1.check(Seq(0, 120)) == false)
    assert(d1.encode(Seq(3, -2)).get.mkString(":") == "3:-61")
  }

  val jsonRange =
    """
      |{
      |  "plugin" : "type",
      |  "type" : "range",
      |  "name" : "hello",
      |  "range_size" : 5,
      |  "range_signed" : 0,
      |  "range_min" : 0,
      |  "range_max" : 10
      |}
    """.stripMargin

  test("range test") {
    val m = util.json.Json.parse(jsonRange)
    var d1 = FromJson.getRange(m)
    assert(d1.name == "hello")
    assert(d1.check(0) == true)
    assert(d1.check(120) == false)
    assert(d1.encode(10).get.mkString(":") == "10")

    assert(m.get("plugin").get == "type")
    assert(m.get("type").get == "range")
    d1 = FromJson.getType(m).asInstanceOf[Range]
    assert(d1.name == "hello")
    assert(d1.check(0) == true)
    assert(d1.check(120) == false)
    assert(d1.encode(10).get.mkString(":") == "10")
  }

  /*
         class Float(override val name: JString = "float",
            val ranges:Seq[JFloat] = null,
            val shift:JFloat = 0.0f
            )
   */
  val jsonFloat =
    """
      |{
      |  "plugin" : "type",
      |  "type" : "float",
      |  "name" : "hello",
      |  "range_min" : -10.0,
      |  "range_max" : 10.0,
      |  "shift" : 1.0
      |}
    """.stripMargin

  test("float test") {
    val m = util.json.Json.parse(jsonFloat)
    var d1 = FromJson.getFloat(m)
    assert(d1.name == "hello")
    assert(d1.check(0.0f) == true)
    assert(d1.check(120.0f) == false)
    assert(d1.encode(10.0f).get.mkString(":") == "65:48:0:0")

    assert(m.get("plugin").get == "type")
    assert(m.get("type").get == "float")
    d1 = FromJson.getType(m).asInstanceOf[Float]
    assert(d1.name == "hello")
    assert(d1.check(0.0f) == true)
    assert(d1.check(120.0f) == false)
    assert(d1.encode(10.0f).get.mkString(":") == "65:48:0:0")
  }

  /*
       class String(override val name:JString = "string",
             min:scala.Char = 0.toChar,
             max:scala.Char = 0.toChar,
             conditions:Seq[Any] = Seq[Any]())
       extends Base[JString](name) with Checker {
 */
  val jsonString =
    """
      |{
      |  "plugin" : "type",
      |  "type" : "string",
      |  "name" : "hello",
      |  "range_min" : "a",
      |  "range_max" : "b",
      |  "condition" : "maxlength",
      |  "limit" : 10
      |}
    """.stripMargin

  test("string test") {
    val m = util.json.Json.parse(jsonString)
    var d1 = FromJson.getString(m)
    assert(d1.name == "hello")
    assert(d1.check("aabbaa") == true)
    assert(d1.check("abcdef") == false)
    assert(d1.check("aaaaabbbbbaaaa") == false)

    assert(m.get("plugin").get == "type")
    assert(m.get("type").get == "string")
    d1 = FromJson.getType(m).asInstanceOf[String]
    assert(d1.name == "hello")
    assert(d1.check("aabbaa") == true)
    assert(d1.check("abcdef") == false)
    assert(d1.check("aaaaabbbbbaaaa") == false)
  }
}
