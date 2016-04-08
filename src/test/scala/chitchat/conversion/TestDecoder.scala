package chitchat.conversion

import org.scalatest.FunSuite

class TestDecoder extends FunSuite {

  test ("decoder with Q=2 & Q = 8") {
    val USER_PROVIDED_TYPE_DIRECTORY = ""
    val typeInstances: Map[String, chitchat.types.Base[_]] = util.types.Util.getTypeInstances(USER_PROVIDED_TYPE_DIRECTORY)
    val typeInference = new util.types.TypeInference(typeInstances)

    val encoder = new Encoder(typeInference)
    val decoder = new Decoder(typeInference)

    var typeFromLabel = typeInference.getChitchatTypeFromLabel("time").get

    println(typeFromLabel.name)

//    var (valueType, instance) = typeFromLabel.get
//    var encoded = encoder.encode("time", List(12,11))
//    assert(decoder.decode(valueType = valueType, instance = instance, ba = encoded).get == List(12,11))
//
//    typeFromLabel = typeInference.getChitchatTypeFromLabel("age")
//    val (valueType2, instance2) = typeFromLabel.get
//    encoded = encoder.encode("age", 11)
//    assert(decoder.decode(valueType = valueType2, instance = instance2, ba = encoded).get == 11)
//
//    typeFromLabel = typeInference.getChitchatTypeFromLabel("string")
//    val (valueType3, instance3) = typeFromLabel.get
//    encoded = encoder.encode("string", "Hello, world")
//    assert(decoder.decode(valueType = valueType3, instance = instance3, ba = encoded).get == "Hello, world")
//
  }
}
