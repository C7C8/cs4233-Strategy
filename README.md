# Strategy

This implementation of Strategy is headless, you'll have to
rely on unit tests. It does output a copy of the board after
every move for debugging purposes, though.

## Build

**Please use the Gradle import wizard for Eclipse!** You don't need
to install anything, just feed the project to the Gradle import
wizard and it'll treat it just like any other project.

To build and run, use the included Gradle wrapper. The "test"
task will run all tests. Eclipse will also be able to do this
directly.

## Package Structure

This project complies with the package structure required,
although folders are a little different:

* Source files are under `src/main/java`
* Test files are under `src/test/java`

This is an extremely common Java project layout, it will be
automatically recognized by Eclipse when imported using the
Gradle import wizard.