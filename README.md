# ls in Java

This project reimplements the basics of `ls` in Java, in particular implementing usage with any number of arguments along with the -a and -l options.

## Building this project

In the `ls` directory, run:

```
mvn package
```

The project can now be run as:

```
java -cp target/ls-1.0-SNAPSHOT.jar com.GabrielRavier.ListDirectoryContents.App [normal-ls-arguments]
```

