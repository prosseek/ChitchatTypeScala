### [2016/03/31]03:02PM

User defined type's name should indicate if the type is range type or encoded type from its name.

    * range_XYZ // this name indicates the type is in Bit
    * encoded_ZYZ // this name indicates the type is in Encoded

### [2016/03/25]10:00PM

Execute test with the generated jar file. 

	SCALATEST=/Volumes/SD/Dropbox/smcho/bin/jar/scalatest/scalatest_2.11-2.2.6.jar:/Users/smcho/github/ChitchatTypeScala/out/artifacts/chitchattypescala_jar/chitchattypescala.jar
	# http://www.scalatest.org/quick_start
	scalac -cp $SCALATEST /Users/smcho/github/ChitchatTypeScala/src/test/scala/util/TestBitSetTool.scala
	scala -cp $SCALATEST org.scalatest.run util.TestBitSetTool