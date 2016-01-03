package com.bt.coding_test.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Unit tests for the package dependencies data file validator.
 */
public class PackageDependenciesDataFileValidatorTest {

    @Test
    public void testInvalidSeparator() {
        assertFalse(PackageDependenciesDataFileValidator.getInstance().isValidSeparator("-<"));
    }
    
    @Test
    public void testValidSeparator() {
        assertTrue(PackageDependenciesDataFileValidator.getInstance().isValidSeparator("->"));
    }
    
    @Test
    public void testInvalidPackageName() {
        assertFalse(PackageDependenciesDataFileValidator.getInstance().isValidPackageName("Invalid.Package.Name"));
    }
    
    @Test
    public void testValidPackageName() {
        assertTrue(PackageDependenciesDataFileValidator.getInstance().isValidPackageName("Valid_Package_Name"));
    }
    
}
