### Preparation for util.file.TestUtil test

* Execute `sbt package` to create the jar file in `target` directory
* Execute `runme.sh` in `<CURRENT_DIRECTORY>/src/test/resources/util/file` directory to create class files in `chitchat/types` sub-directory.
* Make sure in Project Structure -> Modules has dependency `<CURRENT_DIRECTORY>/src/test/resources/util/file`
    * Make the scope as "Test" not the default "Compile".

----
* [2016/03/28]10:06AM
