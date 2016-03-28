1. In Project Structure/Modules, you should add "<CURRENT_DIRECTORY>/src/test/resources/util/file" as Dependencies.
    * Make the scope as "Test" not the default "Compile".
    * Then, run `runme.sh` in the directory to create class files in `modules` directory. 
	* This is required for util/file/TestUtil.scala test. 
    
----
* [2016/03/28]10:06AM
