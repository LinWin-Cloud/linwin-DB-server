find . -type f | grep .java > source.txt
javac -d ../../out/Distributed_Database/ @source.txt
cd ../../out/Distributed_Database/
java MainApp