package chitchat.types

import org.scalatest.FunSuite

import util.json.Json


class TestJsonGeneratedTypes extends FunSuite {

  val json1 =
    """
      |{
      |  "type" : "range",
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

  test ("simple") {
    val m = Json.parse(json1)
    val d1 = JsonGeneration.getEncoding(m)
    println(d1.check(Seq(0, 0)))
    println(d1.check(Seq(0, 120)))
    println(d1.encode(Seq(3, -2)).mkString(":"))
  }
}
