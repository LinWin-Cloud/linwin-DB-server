find . -type f | grep .java > source.txt
javac -d ../../out/Mys-Script/ @source.txt
cd ../../out/Mys-Script/
java Mys $1 $2 $3
