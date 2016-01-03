package com.bt.coding_test;

import java.io.IOException;
import java.util.Set;

import com.bt.coding_test.data.PackageDependenciesDataFileReader;
import com.bt.coding_test.exception.InvalidInputException;
import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;
import com.bt.coding_test.validation.PackageDependenciesDataFileValidator;

/**
 * Class for reading and printing package dependencies.
 */
public class PackageDependenciesProcessor {

    private static final String OUT_PACKAGE_SEPARATOR       = " ";
    private static final String OUT_DEPENDENCIES_SEPARATOR  = " ->";
    
    private static final String ERR_MSG_PREFIX                  = "[ ERROR ] ";
    private static final String ERR_INVALID_PACKAGE_NAME_END    = "\" provided as a command line argument is not valid because it is not defined by the regular expression \"[a-zA-Z0-9_]+\". Please change.";
    private static final String ERR_INVALID_PACKAGE_NAME_BEGIN  = "The package name \"";
    private static final String ERR_INVALID_NR_CMD_LINE_ARGS    = "Usage: java com.bt.coding_test.PackageDependenciesProcessor <package-dependencies-data-file-path> <package-1> <package-2> ... <package-n>.";

    private static PackageDependenciesDirectedGraph packageDependenciesGraph;
    
    
    public static void main(String args[]) {
        try {
            run(args);
        } catch (IOException exception) {
            System.out.println(ERR_MSG_PREFIX + exception.getMessage());
        }
    }
    
    /**
     * Run the package dependency processor for the given command line arguments.
     * 
     * @param args The command line arguments.
     * @throws IOException if command line arguments or data file are invalid.
     */
    public static void run(String args[]) throws IOException {
        validateCommandLineArguments(args);
        readPackageDependencyGraph(args[0]);
        outputPackageDependencies(args);
    }

    /**
     * Check if the provided command line arguments are valid.
     * 
     * @param args The command line arguments.
     * @throws IOException if the command line arguments are invalid.
     */
    private static void validateCommandLineArguments(String[] args) throws IOException {
        // Check if the number of command line arguments is greater than 0
        if (args.length < 1) {
            throw new InvalidInputException(ERR_INVALID_NR_CMD_LINE_ARGS);
        }
        
        // Check if the package names are valid
        for (int i = 1; i < args.length; ++i) {
            if (!PackageDependenciesDataFileValidator.getInstance().isValidPackageName(args[i])) {
                throw new InvalidInputException(
                    ERR_INVALID_PACKAGE_NAME_BEGIN + 
                    args[i] + 
                    ERR_INVALID_PACKAGE_NAME_END
                );
            }
        }
    }
    
    /**
     * Read the package dependency graph from the data file.
     * 
     * @param dataFilePath The data file path.
     * @throws InvalidInputException if the data file is invalid.
     */
    private static void readPackageDependencyGraph(String dataFilePath) throws InvalidInputException {
        PackageDependenciesDataFileReader reader = PackageDependenciesDataFileReader.getInstance();
        
        packageDependenciesGraph = reader.read(dataFilePath);
    }
    
    /**
     * Output the dependencies for all the package names provided as command line arguments.
     * 
     * @param args The command line arguments.
     */
    private static void outputPackageDependencies(String[] args) {
        for (int i = 1; i < args.length; ++i) {
            outputPackageDependency(args[i]);
        }
    }

    /**
     * Output the dependencies for the given package.
     * 
     * @param packageName The package name.
     */
    private static void outputPackageDependency(String packageName) {
       System.out.print(packageName + OUT_DEPENDENCIES_SEPARATOR);
       
       // Output the package dependencies
       Set<Package> dependencies = packageDependenciesGraph.getAllPackageDependencies(new Package(packageName));
       
       for (Package packageDependency : dependencies) {
           System.out.print(OUT_PACKAGE_SEPARATOR + packageDependency);
       }
       
       // Output a new line
       System.out.println("");
    }
    
}
