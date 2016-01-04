package com.bt.coding_test.controller;

import java.io.IOException;
import java.util.Set;

import com.bt.coding_test.data.PackageDependenciesDataFileReader;
import com.bt.coding_test.exception.InvalidInputException;
import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;
import com.bt.coding_test.service.PackageDependenciesFindingStrategy;
import com.bt.coding_test.validation.PackageDependenciesDataFileValidator;

public class PackageDependenciesProcessingController {

    private static final String OUT_PACKAGE_SEPARATOR       = " ";
    private static final String OUT_DEPENDENCIES_SEPARATOR  = " ->";
    
    private static final String ERR_INVALID_PACKAGE_NAME_END    = "\" provided as a command line argument is not valid because it is not defined by the regular expression \"[a-zA-Z0-9_]+\". Please change.";
    private static final String ERR_INVALID_PACKAGE_NAME_BEGIN  = "The package name \"";
    private static final String ERR_INVALID_NR_CMD_LINE_ARGS    = "Usage: java com.bt.coding_test.PackageDependenciesProcessor <package-dependencies-data-file-path> <package-1> [<package-2> ... <package-n>].";

    private PackageDependenciesDirectedGraph     packageDependenciesGraph;
    private PackageDependenciesFindingStrategy   packageDependenciesFinder;
    
    /**
     * Constructor with parameters.
     * 
     * @param strategy The strategy to be used for finding package dependencies.
     */
    public PackageDependenciesProcessingController(PackageDependenciesFindingStrategy strategy) {
        packageDependenciesFinder = strategy;
    }
    
    /**
     * Process the package dependencies considering the given command line arguments.
     * 
     * @param args The command line arguments.
     * @throws IOException if command line arguments or data file are invalid.
     */
    public void run(String args[]) throws IOException {
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
    private void validateCommandLineArguments(String[] args) throws IOException {
        validateNrOfCommandLineArguments(args);
        validateCommandLinePackageNames(args);
    }

    /**
     * Check if the number of command line arguments is greater than 1.
     * 
     * @param args The command line arguments.
     * @throws InvalidInputException if the number of command line arguments is less than 2.
     */
    private void validateNrOfCommandLineArguments(String[] args) throws InvalidInputException {
        if (args.length < 2) {
            throw new InvalidInputException(ERR_INVALID_NR_CMD_LINE_ARGS);
        }
    }
    
    /**
     * Check if the package names provided as command line arguments are valid.
     * 
     * @param args The command line arguments.
     * @throws InvalidInputException if package names are invalid.
     */
    private void validateCommandLinePackageNames(String[] args) throws InvalidInputException {
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
    private void readPackageDependencyGraph(String dataFilePath) throws InvalidInputException {
        PackageDependenciesDataFileReader reader = PackageDependenciesDataFileReader.getInstance();
        
        packageDependenciesGraph = reader.read(dataFilePath);
    }
    
    /**
     * Output the dependencies for all the package names provided as command line arguments.
     * 
     * @param args The command line arguments.
     */
    private void outputPackageDependencies(String[] args) {
        for (int i = 1; i < args.length; ++i) {
            outputPackageDependency(args[i]);
        }
    }

    /**
     * Output the dependencies for the given package.
     * 
     * @param packageName The package name.
     */
    private void outputPackageDependency(String packageName) {
       System.out.print(packageName + OUT_DEPENDENCIES_SEPARATOR);
       
       // Output the package dependencies
       Set<Package> dependencies = packageDependenciesFinder.find(new Package(packageName), packageDependenciesGraph);
       
       for (Package packageDependency : dependencies) {
           System.out.print(OUT_PACKAGE_SEPARATOR + packageDependency);
       }
       
       // Output a new line
       System.out.println("");
    }
    
}
