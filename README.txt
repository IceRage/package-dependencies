~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Package dependencies project
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Author: Ovidiu Parvu
Created on: 2nd January 2016


I) Prerequisites to build and run executables:
    Java SDK 1.8, Maven 3.3.9, JUnit 4.12

II) Compile project and run all unit tests from the project root folder using the command:
    mvn clean install

III) Run package dependencies processor (i.e. executable) from the "target/classes" subfolder using the command:
    java com.bt.coding_test.PackageDependenciesProcessor <package-dependencies-data-file-path> <package-name-1> [<package-name-2> ... <package-name-n>]

IV) Remarks:
    1. Assumptions are described in the comments of the relevant methods. The description of each assumption is preceded by the "Assumption:" prefix.