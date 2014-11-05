all:
	javac -cp lib/json-simple-1.1.1.jar src/rem/*.java
	mv src/rem/*.class bin/rem/

jar:
	jar cmf bin/MANIFEST.MF/ run.jar bin/rem/*.class

run:
	java -jar run.jar
