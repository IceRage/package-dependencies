package com.bt.coding_test.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class used to validate tokens in the package dependencies data file.
 * The class is implemented using the Singleton design pattern.
 */
public class PackageDependenciesDataFileValidator {

    private final static Pattern PACKAGE_PATTERN                = Pattern.compile("[a-zA-Z0-9_]+"); 
    private final static String  PACKAGE_DEPENDENCY_SEPARATOR   = "->";
    
    private static PackageDependenciesDataFileValidator instance = null;
    
    
    public static PackageDependenciesDataFileValidator getInstance() {
        if (instance == null) {
            instance = new PackageDependenciesDataFileValidator();
        }
        
        return instance;
    }
    
    /**
     * Check if the given package name is valid.
     *
     * Assumption: A package name is valid if it is defined by the regular expression "[a-zA-Z0-9_]+".
     * 
     * @param givenPackageName The given package name.
     * @return True if the package name is valid, and false otherwise.
     */
    public boolean isValidPackageName(String givenPackageName) {
        Matcher packagePatternMatcher = PACKAGE_PATTERN.matcher(givenPackageName);
        
        return packagePatternMatcher.matches();
    }
    
    /**
     * Check if the given separator is valid.
     * 
     * @param givenSeparator The given separator.
     * @return True if the separator is valid, and false otherwise.
     */
    public boolean isValidSeparator(String givenSeparator) {
        return (givenSeparator.compareTo(PACKAGE_DEPENDENCY_SEPARATOR) == 0);
    }
    
    private PackageDependenciesDataFileValidator() {
        // Do nothing
    }
    
}
