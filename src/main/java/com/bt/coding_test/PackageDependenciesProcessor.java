package com.bt.coding_test;

import java.io.IOException;

import com.bt.coding_test.controller.PackageDependenciesProcessingController;
import com.bt.coding_test.service.DFSPackageDependenciesFindingStrategy;
import com.bt.coding_test.service.PackageDependenciesFindingStrategy;

/**
 * Class for reading and printing package dependencies.
 */
public class PackageDependenciesProcessor {

    private static final String ERR_MSG_PREFIX = "[ ERROR ] ";
    
    public static void main(String args[]) {
        try {
            PackageDependenciesFindingStrategy      strategy   = new DFSPackageDependenciesFindingStrategy();
            PackageDependenciesProcessingController controller = new PackageDependenciesProcessingController(strategy);
            
            controller.run(args);
        } catch (IOException exception) {
            System.err.println(ERR_MSG_PREFIX + exception.getMessage());
        }
    }
    
}
