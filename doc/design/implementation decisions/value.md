### [2016/04/07]05:06PM
We need a value type.

### X Wrong! Just record [2016/04/07]05:03PM

The class is just a class, and has a value member.
From user's perspective, it's best to use object's contstructor without
using the `new` operator.

    object String {
      def apply(value:JString) = {
        val string = new String
        string.value = value
        string
      }
    }

    class String(override val name:JString = "string") extends Base[JString](name) with Checker {

      var value:JString = _