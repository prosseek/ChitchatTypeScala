### [2016/03/25]10:00PM

Execute test with the generated jar file. 

	SCALATEST=/Volumes/SD/Dropbox/smcho/bin/jar/scalatest/scalatest_2.11-2.2.6.jar:/Users/smcho/github/ChitchatTypeScala/out/artifacts/chitchattypescala_jar/chitchattypescala.jar
	# http://www.scalatest.org/quick_start
	scalac -cp $SCALATEST /Users/smcho/github/ChitchatTypeScala/src/test/scala/util/TestBitSetTool.scala
	scala -cp $SCALATEST org.scalatest.run util.TestBitSetTool