package com.bt.coding_test;

import java.io.IOException;

import org.junit.Test;

import com.bt.coding_test.exception.InvalidInputException;

public class PackageDependenciesProcessorTest {

    @Test(expected = InvalidInputException.class)
    public void testNoCommandLineArguments() throws IOException {
        String[] args = new String[0];
        
        PackageDependenciesProcessor.run(args);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidPackageNamesProvidedAsCommandLineArguments() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "gui";
        args[2] = "swingui#";
        
        PackageDependenciesProcessor.run(args);
    }
    
    @Test
    public void testNoPackageNamesProvidedAsCommandLineArguments() throws IOException {
        String[] args = new String[1];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        
        PackageDependenciesProcessor.run(args);
    }
    
    @Test
    public void testPackageNamesNotIncludedInDataFile() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "unknown";
        args[2] = "swingui";
        
        PackageDependenciesProcessor.run(args);
    }
    
    @Test
    public void testValidPackageNamesAndDataFile() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "gui";
        args[2] = "swingui";
        
        PackageDependenciesProcessor.run(args);
    }
    
}
