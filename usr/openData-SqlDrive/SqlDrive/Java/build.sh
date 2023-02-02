find . -type f | grep .java > source.txt
javac -d ./class/  @source.txt
cd class
java test
