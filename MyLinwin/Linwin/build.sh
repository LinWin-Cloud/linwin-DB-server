rm -f *.class
rm -f ./LinwinVOS/*.class
rm -f ./LinwinVOS/runtime/*.class
rm -f ./LinwinVOS/Users/*.class
javac *.java
javac ./LinwinVOS/runtime/*.java
java MyLinwin
