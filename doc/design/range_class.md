### [2016/04/19]01:48PM

Range class has six elements in three groups.

* name
* size/signed/min/max
* correlatedLabels

The example is as follows:

    class Range(override val name:java.lang.String = "",
                var size:Int = 0,
                val signed:scala.Boolean = false,
                val min:Int = 0,
                val max:Int = 0,
                override val correlatedLabels:Seq[java.lang.String] = Seq[java.lang.String]())