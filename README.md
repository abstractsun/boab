# boab 🌳

**boab** is a simple command line utility for working at the root folder of a workspace regardless of what subdirectory you are in. It is currently available as a native Linux executable and a cross-platform java .jar file.

## Usage

A boab "workspace" can be created in any folder:

```
cd /path/to/project
boab init
```

This creates the folder `/path/to/project/.boab`. `/path/to/project` is then considered the "root folder" of your workspace.

Then you can do things like... [open](https://github.com/abstractsun/misc-tools/blob/master/xopen) the root folder of your workspace:

```sh
cd /path/to/project/oh/no/im/trapped/in/folders
xopen "`boab pwd`"
```

...or run a program with the workspace root folder as the working directory (looking at you, gradle!):

```sh
cd /path/to/project2/src/main/java/oh/no/not/again
boab run -- gradle build
```

The current list of available commands can be viewed with `boab --help`

boab is currently a seedling program. Perhaps someday, with love and care, it will grow into something bigger.

## License

GNU AGPL version 3 or, at your option, any later version.

## See also

- [jenv](https://github.com/jenv/jenv/) - Alternative to setting `JAVA_HOME` that can be set on a per-directory basis (substitute `.bash_profile` for `.bashrc` as needed)
- `pushd`, `popd`, `dirs` - bash builtins for navigating directories like a stack/queue

## Compiling

boab is compiled with [Java Delevopment Kit](https://adoptopenjdk.net/) 11, so `java -version` should return major version 11 in order to run gradlew (other versions of the JDK may also work).

For the cross platform jar, simply run `./gradlew build` and get `build/libs/boab.jar` (or `gradle.bat build` on cmd)

For an [Eclipse](https://www.eclipse.org/downloads/) IDE workspace, use `./gradlew eclipse` (or `gradle.bat eclipse` on cmd).

A statically linked native executable can nontrivially be produced by feeding boab.jar into [GraalVM](https://www.graalvm.org/):

```sh
# Some prerequisite dependencies may be required for compilation
cd /path/to/graalvm-ce-java11-x.y.z/bin
./native-image --static -jar /path/to/boab.jar /desired/path/to/boab
```
