
find . -type f | grep .java > source.txt
javac -d ../../out/ClientUI/  @source.txt
cd ../../out/ClientUI/
jar -cvfm ClientUI.jar ../../release/MANIFEST/ClientUI.MF *
mv ClientUI.jar ../../release/out/
cd ../../release/out/
java -jar ClientUI.jar