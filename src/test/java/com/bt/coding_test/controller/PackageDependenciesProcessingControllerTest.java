package com.bt.coding_test.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.bt.coding_test.exception.InvalidInputException;
import com.bt.coding_test.service.DFSPackageDependenciesFindingStrategy;

public class PackageDependenciesProcessingControllerTest {

    private static final String PERF_TEST_PACKAGE_NAME_PREFIX = "Abracadabra";
    private static final int    PERF_TEST_NR_DEPS_PER_PACKAGE = 100;
    private static final int    PERF_TEST_NR_PACKAGES         = 1000;
    
    private static final String OUT_LINE_SEPARATOR = System.lineSeparator();
    
    private PackageDependenciesProcessingController controller;
    
    @Before
    public void setUp() {
        controller = new PackageDependenciesProcessingController(new DFSPackageDependenciesFindingStrategy());
    }
    
    @Test(expected = InvalidInputException.class)
    public void testNoCommandLineArguments() throws IOException {
        String[] args = new String[0];
        
        controller.run(args);
    }
    
    @Test(expected = InvalidInputException.class)
    public void testInvalidPackageNamesProvidedAsCommandLineArguments() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "gui";
        args[2] = "swingui#";
        
        controller.run(args);
    }
    
    @Test
    public void testNoPackageNamesProvidedAsCommandLineArguments() throws IOException {
        String[] args = new String[1];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        
        controller.run(args);
    }
    
    @Test
    public void testPackageNamesNotIncludedInDataFile() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "unknown";
        args[2] = "swingui";
        
        controller.run(args);
    }
    
    @Test
    public void testValidPackageNamesAndDataFile() throws IOException {
        String[] args = new String[3];
        
        args[0] = getClass().getResource("data_file.txt").getFile();
        args[1] = "gui";
        args[2] = "swingui";
        
        controller.run(args);
    }
    
    @Test
    public void testManyPackagesWithManyDependencies() throws IOException {
        String[] args = new String[2];
        
        args[0] = getPerformanceTestFilePath();
        args[1] = PERF_TEST_PACKAGE_NAME_PREFIX + "0";
        
        controller.run(args);
    }

    /**
     * Create a temporary file containing the data for the performance test and return its path.
     * @return The path to the performance test file.
     * @throws IOException 
     */
    private String getPerformanceTestFilePath() throws IOException {
        File tmpFile = File.createTempFile("performance_test", ".txt");
        
        // Request that the temporary file is deleted on exit
        tmpFile.deleteOnExit();
        
        addPerformanceTestDataToFile(tmpFile);
        
        return tmpFile.getAbsolutePath();
    }

    /**
     * Add the performance test data to the given file.
     * @param file The given file.
     * @throws IOException if the performance test data cannot be written to the file.
     */
    private void addPerformanceTestDataToFile(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        
        for (int i = 0; i < PERF_TEST_NR_PACKAGES; ++i) {
            writer.write(PERF_TEST_PACKAGE_NAME_PREFIX + i + " ->");
            
            for (int j = 0; j < PERF_TEST_NR_DEPS_PER_PACKAGE; ++j) {
                writer.write(" " + PERF_TEST_PACKAGE_NAME_PREFIX + ((i + j + 1) % PERF_TEST_NR_PACKAGES));
            }
            
            writer.write(OUT_LINE_SEPARATOR);
        }
        
        writer.close();
    }
    
}
