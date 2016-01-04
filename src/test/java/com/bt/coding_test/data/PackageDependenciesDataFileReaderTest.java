package com.bt.coding_test.data;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.bt.coding_test.exception.InvalidInputException;
import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;


public class PackageDependenciesDataFileReaderTest {

    private PackageDependenciesDataFileReader reader;
    
    @Before
    public void setUp() {
        reader = PackageDependenciesDataFileReader.getInstance();
    }
    
    @Test
    public void testValidDataFilePath() throws InvalidInputException {
        String filePath = getClass().getResource("data_file_valid.txt").getFile();
        
        readAndValidatePackageDependencyGraphFromValidDataFile(filePath);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidDataFilePath() throws InvalidInputException {
        String filePath = "data_file_non_existent.txt";
        
        reader.read(filePath);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidDataFileFirstPackageMissing() throws InvalidInputException {
        String filePath = "data_file_invalid_first_package_missing.txt";
        
        reader.read(filePath);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidPackageSelfDependency() throws InvalidInputException {
        String filePath = "data_file_invalid_package_self_dependency.txt";
        
        reader.read(filePath);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidDataFileSeparatorTokenMissing() throws InvalidInputException {
        String filePath = "data_file_invalid_separator_token_missing.txt";
        
        reader.read(filePath);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidDataFileWrongPackageName() throws InvalidInputException {
        String filePath = "data_file_invalid_wrong_package_name.txt";
        
        reader.read(filePath);
    }
    
    @Test
    public void testValidDataFileEmpty() throws InvalidInputException {
        String filePath = getClass().getResource("data_file_valid_empty.txt").getFile();
        
        reader.read(filePath);
    }
    
    @Test
    public void testValidDataFileIndependentPackages() throws InvalidInputException {
        String filePath = getClass().getResource("data_file_valid_independent_packages.txt").getFile();
        
        reader.read(filePath);
    }
    
    @Test
    public void testValidDataFileWithBlankLines() throws InvalidInputException {
        String filePath = getClass().getResource("data_file_valid_with_blank_lines.txt").getFile();
        
        readAndValidatePackageDependencyGraphFromValidDataFile(filePath);
    }
    
    @Test
    public void testValidDataFileExtraWhiteSpace() throws InvalidInputException {
        String filePath = getClass().getResource("data_file_valid_extra_white_space.txt").getFile();
        
        readAndValidatePackageDependencyGraphFromValidDataFile(filePath);
    }
    
    /**
     * Read and validate package dependency graph from valid data file.
     * 
     * @param filePath The data file path. 
     * @throws InvalidInputException if the data file path is invalid, the data file contents cannot be read 
     *         or the data file contents are invalid. 
     */
    private void readAndValidatePackageDependencyGraphFromValidDataFile(String filePath) throws InvalidInputException {
        PackageDependenciesDirectedGraph graph = reader.read(filePath);
        
        // Check if the direct dependencies of gui are correct
        Iterator<Package> packageDependencies = graph.getDirectPackageDependencies(new Package("gui"));
        
        assertTrue(packageDependencies.next().getName().equals("awtui"));
        assertTrue(packageDependencies.next().getName().equals("swingui"));
        assertFalse(packageDependencies.hasNext());
        
        // Check if the direct dependencies of swingui are correct
        packageDependencies = graph.getDirectPackageDependencies(new Package("swingui"));
        
        assertTrue(packageDependencies.next().getName().equals("extensions"));
        assertTrue(packageDependencies.next().getName().equals("runner"));
        assertFalse(packageDependencies.hasNext());
        
        // Check if the direct dependencies of textui are correct
        packageDependencies = graph.getDirectPackageDependencies(new Package("textui"));
        
        assertTrue(packageDependencies.next().getName().equals("framework"));
        assertTrue(packageDependencies.next().getName().equals("runner"));
        assertFalse(packageDependencies.hasNext());
        
        // Check if the direct dependencies of awtui are correct
        packageDependencies = graph.getDirectPackageDependencies(new Package("awtui"));
        
        assertTrue(packageDependencies.next().getName().equals("runner"));
        assertFalse(packageDependencies.hasNext());
        
        // Check if the direct dependencies of runner are correct
        packageDependencies = graph.getDirectPackageDependencies(new Package("runner"));
        
        assertTrue(packageDependencies.next().getName().equals("framework"));
        assertFalse(packageDependencies.hasNext());
        
        // Check if the direct dependencies of extensions are correct
        packageDependencies = graph.getDirectPackageDependencies(new Package("extensions"));
        
        assertTrue(packageDependencies.next().getName().equals("framework"));
        assertFalse(packageDependencies.hasNext());
    }
    
}
