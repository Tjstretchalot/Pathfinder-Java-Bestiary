# Pathfinder-Java-Bestiary
A bare-bones creature-environment-climate bestiary for Pathfinder in Java. Containing a complete test suite as well as documentation, this is ready for further expansion to include more creature information if the need arises.

## Installation
The project was created in Eclipse, and the installation instructions assume Mars Eclipse, however they can be easily adapted for other development environments.

1. Create the project in eclipse at the desired location in the desired workspace. [Information](http://help.eclipse.org/mars/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2FgettingStarted%2Fqs-3.htm). Do not close eclipse. This was built using jdk 1.8.0_60, and requires atleast Java version 8.
2. Download [log4j2](http://logging.apache.org/log4j/2.x/). Add log4j-api-2.x.jar and log4j-api-2.x.jar to the build path. This project was built using log4j 2.0 [How to add to the build path](http://help.eclipse.org/juno/index.jsp?topic=%2Forg.eclipse.jdt.doc.user%2Freference%2Fref-properties-build-path.htm)
3. Download the [sqlite jdbc by xerial](https://bitbucket.org/xerial/sqlite-jdbc/downloads). Add sqlite-jdbc-3.8.x.x.jar to the build path. This project was build using sqlite-jdbc-3.8.11.2.jar 
4. Add JUnit 4 to the build path. JUnit 4 should be installed by default in eclipse, but information about download and installation is available [here](http://junit.org/).
5. Download this project either by downloading the zip or forking and pulling, or just pulling. [More information about forking](https://help.github.com/articles/fork-a-repo/). Extract directly into the directory the project is in, such that the downloaded src folder overwrites the original src folder.
6. Open eclipse, highlight the project in the project explorer on the left, and press F5 to refresh the project. This should populate eclipses model.
7. *Optional* Copy or move the pathfinder.db to <user.home>/My Games/Pathfinder/pathfinder.db for it to be used as the default database by the SqlitePersisterFactory.

## Usage
See the documentation for me.timothy.pathfinder.bestiary.persisters.Persister for a general overview of how to use this database. See me.timothy.pathfinder.bestiary.apps.EncounterTables for a primitive example of generating the encounter tables. See any JUnit test case for the sqlite library for examples on how to use any specific Persister. 
