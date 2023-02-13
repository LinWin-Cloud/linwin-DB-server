javac -d ../../out/client/ *.java
cd ../../out/client/
jar -cvfm ClientShell.jar ../../release/MANIFEST/ClientShell.MF *.class
mv ClientShell.jar ../../release/out/
cd ../../release/out/