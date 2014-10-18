all:
	javac src/rem/*.java
	mv src/rem/*.class bin/rem/

jar:
	jar cmf bin/MANIFEST.MF/ run.jar bin/rem/*.class

