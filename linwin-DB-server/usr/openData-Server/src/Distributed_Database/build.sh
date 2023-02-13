find . -type f | grep .java > source.txt
javac -d ../../out/Distributed_Database/ @source.txt
cd ../../out/Distributed_Database/
jar -cvfm Distributed_Database.jar ../../release/MANIFEST/Distributed_Database.MF *
mv Distributed_Database.jar ../../release/out
cd ../../release/out
java -jar Distributed_Database.jar
