package chitchat.typefactory

import org.scalatest.FunSuite

class TestTypeDatabaseDefault extends FunSuite {

  test ("when value is int -> stored as short") {
    val types = FromClass.getTypeInstances("")
    val typeDatabase = TypeDatabase(Seq[java.lang.String](""))
    val tm = typeDatabase.encode("hello", 10).get
    assert(tm.mkString(":") == "0:10")
  }
//
//  test ("when label ends with _i") {
//    val types = FromClass.getTypeInstances("")
//    val typeDatabase = TypeDatabase(Seq[java.lang.String](""))
//
//    val ba = typeDatabase.encode("hello_i", 10).get
//    val tm = typeDatabase.decode("hello_i", ba).get
//    assert(tm == 10)
//  }
}
