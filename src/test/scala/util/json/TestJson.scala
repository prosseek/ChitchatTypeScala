package util.json

import org.scalatest.FunSuite

class TestJson extends FunSuite {

  val jsonString = """{
    "string": "James",
    "age": 10,
    "date": [10, 3, 17],
    "time": [12, 14],
    "latitude": [1, 2, 3, 4],
    "longitude": [11, 12, 13, 14]
  }"""

  test ("simple parser") {
    val m = Json.parse(jsonString)
    assert(m("string") == "James")
    assert(m("age") == 10)
    assert(m("date") == Seq(10,3,17))
    assert(m("time") == Seq(12, 14))
    assert(m("latitude") == Seq(1,2,3,4))
    assert(m("longitude") == Seq(11,12,13,14))
  }

  test ("simple json file read") {
    val m = Json.loadJson("./src/test/resources/jsonFiles/simple_example/simple.json")
    assert(m("string") == "James")
    assert(m("age") == 10)
    assert(m("date") == Seq(10,3,17))
    assert(m("time") == Seq(12, 14))
    assert(m("latitude") == Seq(1,2,3,4))
    assert(m("longitude") == Seq(11,12,13,14))
  }

  test ("save test") {
    val savePath = "./src/test/resources/jsonFiles/simple_example/simple_result.json"
    val mp = Json.parse(jsonString)
    Json.save(savePath, mp)
    val m = Json.loadJson(savePath)

    m foreach {
      case (k, v) => assert(v == mp(k))
    }
  }

  test("is simple json") {
    val gmapPath = "./src/test/resources/jsonFiles/complex_example/color.json"
    val mp = Json.loadJson(gmapPath)
    // val content = Json.loadJsonContent(gmapPath)
    assert(Json.isSimpleJson(mp) == false)

    val savePath = "./src/test/resources/jsonFiles/simple_example/simple_result.json"
    val mp2 = Json.loadJson(savePath)
    assert(Json.isSimpleJson(mp2) == true)
  }

  test("complex test") {
    {
      val gmapPath = "./src/test/resources/jsonFiles/complex_example/google_map.json"
      intercept[JsonException] {
        val mp = Json.loadJson(gmapPath)
        // "point":new GLatLng(40.266044,-74.718479),
        println(mp.mkString(":"))
      }
    }
    {
      val gmapPath = "./src/test/resources/jsonFiles/complex_example/color.json"
      val mp = Json.loadJson(gmapPath)
      // val content = Json.loadJsonContent(gmapPath)
      assert(Json.build(mp).toString.startsWith("{\"colorsArray\":"))
    }
  }
}

