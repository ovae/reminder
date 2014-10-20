reminder
========

A small program to write down your tasks.

##Getting Started

* Download the program

* Install make 

* Open a terminal of your choice. Change in the JstreamWriter directory
and enter the following command:
```
	$> make
```

you should get something like this:
```
	username@Computername ~/.../reminder $ make
		javac src/rem/*.java
		mv src/rem/*.class bin/rem/
	username@Computername ~/.../reminder $ 
```

* To build a jar file enter:
```
	$> make jar
```

you should get something like that:
```
	username@Computername ~/.../reminder $ make
		jar cmf bin/MANIFEST.MF/ run.jar bin/rem/*.class
	username@Computername ~/.../reminder $ 
```

* If you are using a unix, or unix like operating system
	change the chmod to 744.
```
	$> sudo chmod 744 run.jar
```

* To execute the jar file type in
```
	$> java -jar run.jar
```


