package com.bt.coding_test.data;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bt.coding_test.exception.InvalidInputException;
import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;
import com.bt.coding_test.validation.PackageDependenciesDataFileValidator;

/**
 * Class used to read package dependencies and construct a corresponding directed graph.
 * This class implements the Singleton design pattern.
 */
public class PackageDependenciesDataFileReader {

    private static final String ERR_INVALID_DATA_FILE_PATH_BEGIN    = "The data file path \"";
    private static final String ERR_INVALID_DATA_FILE_PATH_END      = "\" is not valid. Please change.";
    private static final String ERR_READ_DATA_FILE                  = "An error occurred while trying to read the data file contents. Please ensure no other process is accessing the data file.";
    
    private static final String ERR_INVALID_NR_TOKENS           = "The number of tokens is less than two";
    private static final String ERR_INVALID_SEPARATOR_TOKEN     = "The separator token (i.e. second token) is not equal to \"->\" as expected";
    private static final String ERR_INVALID_PACKAGE_NAME_BEGIN1 = "The package name \"";
    private static final String ERR_INVALID_PACKAGE_NAME_BEGIN2 = "\" is invalid because it is not defined by the regular expression \"[a-zA-Z0-9_]+\"";
    private static final String ERR_PACKAGE_SELF_DEPENDENCY     = "A package self-dependency, which is not allowed, was given";
    
    private static final String ERR_MSG_MIDDLE1 = " on line \"";
    private static final String ERR_MSG_MIDDLE2 = "\" (Line number: ";
    private static final String ERR_MSG_END     = ") of the data file. Please change.";

    private static PackageDependenciesDataFileReader instance = null;
    
    
    /**
     * Method used to return an instance of the class. 
     * 
     * @return The single instance of the class.
     */
    public static PackageDependenciesDataFileReader getInstance() {
        if (instance == null) {
            instance = new PackageDependenciesDataFileReader();
        }
        
        return instance;
    }
    
    /**
     * Read package dependencies from a file and construct the corresponding directed graph.
     * 
     * @param filePath The path to the file where the package dependencies are stored.
     * @return The directed graph recording all package dependencies.
     * @throws IOException Exception thrown if the file path provided is not valid, cannot read from file, 
     *                     or file contents are invalid.
     */
    public PackageDependenciesDirectedGraph read(String filePath) throws InvalidInputException {
        try {
            return readFromFile(filePath);
        } catch (InvalidInputException exception) {
            // Rethrow exception
            throw exception;
        } catch (FileNotFoundException exception) {
            throw new InvalidInputException(
                ERR_INVALID_DATA_FILE_PATH_BEGIN + 
                filePath + 
                ERR_INVALID_DATA_FILE_PATH_END
            );
        } catch (IOException exception) {
            throw new InvalidInputException(
                 ERR_READ_DATA_FILE
            );
        }
    }
    
    private PackageDependenciesDataFileReader() {
        // DO nothing
    }
    
    /**
     * Read package dependencies from a file and construct the corresponding directed graph.
     * 
     * @param filePath The path to the file where the package dependencies are stored.
     * @return The directed graph recording all package dependencies.
     * @throws IOException Exception thrown if the file path provided is not valid, cannot read from file, 
     *                     or file contents are invalid.
     */
    private PackageDependenciesDirectedGraph readFromFile(String filePath) throws IOException {
        PackageDependenciesDirectedGraph graph      = new PackageDependenciesDirectedGraph();
        BufferedReader                   fileReader = new BufferedReader(new FileReader(filePath));
        
        // Process each line from the data file
        // Assumption: The number of lines is less or equal to 2^63 - 1
        long lineNumber = 1;
        
        while (fileReader.ready()) {
            String line = fileReader.readLine();
            
            processLine(line, lineNumber++, graph);
        }
        
        // Close the file reader
        fileReader.close();
        
        return graph;
    }

    /**
     * Read the package dependencies from the given line and add the dependencies to the graph.
     *  
     * @param line          The given line.
     * @param lineNumber    The given line number.
     * @param graph         The package dependency graph.
     * @throws InvalidInputException if the line is invalid. 
     */
    private void processLine(String line, long lineNumber, PackageDependenciesDirectedGraph graph) 
                             throws InvalidInputException {
        if (!line.isEmpty()) {
            List<String> tokens = splitLineAndValidateTokens(line, lineNumber);
            
            // Add package dependencies to graph
            Package srcPackage = new Package(tokens.get(0));
            
            for (int i = 2; i < tokens.size(); ++i) {
                graph.addNewPackageDependency(
                    srcPackage, 
                    new Package(tokens.get(i))
                );
            }
        }
    }

    /**
     * Split the line into tokens and check if the tokens are valid.
     * 
     * Assumption: A line is invalid if it does not contain one package followed by the separator token "->".
     * Assumption: A line is invalid if it contains a package self-dependency.
     * Assumption: A data file entry is valid if it is defined by the regular expression: 
     *             "[ \t]*<package>[ \t]+->([ \t]+<package>)+[ \t]*", 
     *             where <package> is equal to the regular expression defined to validate package names in the 
     *             com.bt.coding_test.validation.PackageDependenciesDataFileValidator class. 
     * Assumption: It is not an error if the same package name appears more than once to the right of the separator 
     *             token "->". 
     *  
     * @param line          The given line.
     * @param lineNumber    The given line number.
     * @return The tokens computed from the given line.
     * @throws InvalidInputException if the line is invalid.
     */
    private List<String> splitLineAndValidateTokens(String line, long lineNumber) throws InvalidInputException {
        List<String> tokens = splitLineIntoTokens(line);
        
        // Check if the tokens are valid
        validateNrOfTokens(tokens, line, lineNumber);
        validateSeparatorToken(tokens, line, lineNumber);
        validatePackageTokens(tokens, line, lineNumber);
        
        return tokens;
    }

    /**
     * Check if the number of tokens is at least two. 
     * 
     * @param tokens        The given tokens.
     * @param line          The line for which the tokens were computed.
     * @param lineNumber    The line number.
     * @throws InvalidInputException if the number of tokens is below two. 
     */
    private void validateNrOfTokens(List<String> tokens, String line, long lineNumber) throws InvalidInputException {
        if (tokens.size() < 2) {
            throwInvalidInputException(ERR_INVALID_NR_TOKENS, line, lineNumber);
        }
    }
    
    /**
     * Check if the token separating the left and right hand side packages is valid.
     * 
     * @param tokens        The given tokens.
     * @param line          The line for which the tokens were computed.
     * @param lineNumber    The line number.
     * @throws InvalidInputException if the separator token is not "->".
     */
    private void validateSeparatorToken(List<String> tokens, String line, long lineNumber) 
                                        throws InvalidInputException {
        if (!PackageDependenciesDataFileValidator.getInstance().isValidSeparator(tokens.get(1))) {
            throwInvalidInputException(ERR_INVALID_SEPARATOR_TOKEN, line, lineNumber);
        }
    }
    
    /**
     * Check if the package tokens have valid names and that the left hand side package does not occur 
     * on the right hand side.
     * 
     * @param tokens        The given tokens.
     * @param line          The line for which the tokens were computed.
     * @param lineNumber    The line number.
     * @throws InvalidInputException 
     */
    private void validatePackageTokens(List<String> tokens, String line, long lineNumber) throws InvalidInputException {
        // Validate package to the left of the separator token called the source package
        String srcPackageName = tokens.get(0);
        
        validatePackageName(srcPackageName, line, lineNumber);
        
        // Validate the packages to the right of the separator token called the dependency packages
        for (int i = 2; i < tokens.size(); ++i) {
            String dependencyPackageName = tokens.get(i);
            
            validatePackageName(dependencyPackageName, line, lineNumber);

            // Check if the source and dependency package names are identical
            if (srcPackageName.equals(dependencyPackageName)) {
                throwInvalidInputException(ERR_PACKAGE_SELF_DEPENDENCY, line, lineNumber);
            }
        }
    }

    /**
     * Check if the given package name is valid.
     * 
     * @param packageName The name of the package.
     * @param line        The line containing the package name.
     * @param lineNumber  The corresponding line number.
     * @throws InvalidInputException 
     */
    private void validatePackageName(String packageName, String line, long lineNumber) throws InvalidInputException {
        if (!PackageDependenciesDataFileValidator.getInstance().isValidPackageName(packageName)) {
            throwInvalidInputException(
                ERR_INVALID_PACKAGE_NAME_BEGIN1 + packageName + ERR_INVALID_PACKAGE_NAME_BEGIN2, 
                line, 
                lineNumber
            );
        }
    }

    /**
     * Throw an invalid input exception with the given message, line and line number.
     * 
     * @param message       The message of the exception.
     * @param line          The line for which the exception is thrown.
     * @param lineNumber    The line number.
     * @throws InvalidInputException 
     */
    private void throwInvalidInputException(String message, String line, long lineNumber) throws InvalidInputException {
        throw new InvalidInputException(
            message +
            ERR_MSG_MIDDLE1 +
            line +
            ERR_MSG_MIDDLE2 +
            lineNumber +
            ERR_MSG_END
        );
    }

    /**
     * Split the given line into tokens.
     * 
     * Assumption: Tokens are separated by white-space characters " " and "\t".
     * 
     * @param line The given line.
     * @return The tokens computed from the given line.
     */
    private List<String> splitLineIntoTokens(String line) {
        List<String> tokens = new ArrayList<String>();
        
        // Compute tokens from the given line
        int stopIndex = 0;
        
        for (int i = 0; i < line.length(); ++i) {
            if (!isWhiteSpaceCharacter(line.charAt(i))) {
                stopIndex = computeTokenStopIndex(line, i);
                
                tokens.add(
                    line.substring(i, stopIndex + 1)
                );
                
                i = stopIndex;
            }
        }
        
        return tokens;
    }

    /**
     * Compute the stop index for the token starting at idx in the given line.
     *  
     * @param line The given line.
     * @param idx  The index at which the token starts.
     * @return The token stop index.
     */
    private int computeTokenStopIndex(String line, int idx) {
        int stopIndex = idx + 1;
        
        while ((stopIndex < line.length()) && (!isWhiteSpaceCharacter(line.charAt(stopIndex)))) {
            ++stopIndex;
        }
        
        return (stopIndex - 1);
    }

    /**
     * Check if the given character is a white space character (i.e. " " or "\t").
     * 
     * Assumption: The characters which are considered white space characters are " " and "\t".
     * 
     * @param character The given character.
     * @return True if the given character is a white space character, and false otherwise.
     */
    private boolean isWhiteSpaceCharacter(char character) {
        return (
            (character == ' ') ||
            (character == '\t')
        );
    }
    
}
