~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Package dependencies project
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Author: Ovidiu Parvu
Date: 2nd January 2016


I) Prerequisites to build and run executables:
Maven 3.3.9, JUnit 4.12

II) Compile project and run all unit tests from the project root folder using the command:
mvn clean install

III) Run package dependencies processor (i.e. executable) from the "target" subfolder using:
java -cp com.bt.coding_test-1.0.0.jar com.bt.coding_test.PackageDependenciesProcessor <package-dependencies-data-file-path> <package-name-1> <package-name-2> ... <package-name-n>

IV) Remarks:
1. Assumptions are described in the comments of the relevant methods. The description of each assumption is preceded by the "Assumption:" prefix.