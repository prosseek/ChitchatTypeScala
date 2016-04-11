package chitchat.typefactory

import org.scalatest.FunSuite


class TestFromJson extends FunSuite {

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
      |  "range2_max" : 10,
      |  "correlated_count" : 2,
      |  "correlated1" : "x",
      |  "correlated2" : "y"
      |}
    """.stripMargin

  test ("simple") {
    val m = util.json.Json.parse(json1)
    val d1 = FromJson.getEncoding(m)
    assert(d1.check(Seq(0, 0)) == true)
    assert(d1.check(Seq(0, 120)) == false)
    assert(d1.encode(Seq(3, -2)).get.mkString(":") == "3:-61")
    assert(d1.correlated == List("x", "y"))
  }
}
