# Hub

This project allows to turn-on a minecraft server as a lobby for a party server.

## Tools

The server is built using [Kotlin](https://kotlinlang.org/) and [Minestom](https://github.com/Minestom/Minestom).
The version of tools used is specified in the [build.gradle.kts](build.gradle.kts) file.

The entrypoint of the program is located [here](src/main/kotlin/fr/rushy/hub/Main.kt).

## How to use

In order to use the Hub server, you need to compile the program using gradle command:

```bash
gradle shadowJar
# or
gradlew shadowJar
```

When the compilation is done, a jar file will be generated in the [build/libs](build/libs) folder.

_We advise you to move this jar file in a empty directory to turn-on the server._

To turn-on the server, you need to execute the following command:

```bash
java -jar hub.jar
```

When you execute for the first time the server, a configuration file will be generated in the same directory.

You can modify this file to change the server configuration.

### Configuration

You can retrieve the default configuration file [here](src/main/resources/server.conf).

The configuration is generated automatically when you execute the server for the first time.
The format used is [HOCON](https://github.com/lightbend/config#using-hocon-the-json-superset).

When you execute the server, it will load the configuration named `server.conf`.
However, you can define another configuration file to use with the following command:

```bash
java -jar hub.jar /path/to/config/file.conf
```

### Test

The code is tested using JUnit and can be executed using the following command:

```bash
gradle test
# or
gradlew test
```