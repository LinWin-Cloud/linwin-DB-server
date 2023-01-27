rm -fr ../../out/Linwin/*
find . -type f | grep .java > source.txt
javac -d ../../out/Linwin/ @source.txt
cd ../../out/Linwin/
jar -cvfm MyLinwin.jar ../../release/MANIFEST/LinwinDataService.MF *
mv MyLinwin.jar ../../release/out/
cd ../../release/out/
java -jar MyLinwin.jar
