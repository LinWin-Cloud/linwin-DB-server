find . -type f | grep .java > source.txt
javac -d ../../out/Mys-Script/ @source.txt
cd ../../out/Mys-Script/
jar -cvfm Mys.jar ../../release/MANIFEST/Mys.MF *
mv Mys.jar ../../release/out/