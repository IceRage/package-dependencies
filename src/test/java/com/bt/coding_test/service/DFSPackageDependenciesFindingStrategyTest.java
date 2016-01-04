package com.bt.coding_test.service;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;
import com.bt.coding_test.service.DFSPackageDependenciesFindingStrategy;
import com.bt.coding_test.service.PackageDependenciesFindingStrategy;

public class DFSPackageDependenciesFindingStrategyTest {

    private final static Package SRC_PACKAGE = new Package("gui"); 
    
    private PackageDependenciesDirectedGraph    packageDependencies;
    private PackageDependenciesFindingStrategy  packageDependenciesFinder;
    
    
    @Before
    public void setUp() {
        packageDependencies         = new PackageDependenciesDirectedGraph();
        packageDependenciesFinder   = new DFSPackageDependenciesFindingStrategy(); 
    }
    
    @Test
    public void testDependenciesSortedAlphabetically() {
        Package[] targetPackages = {
            new Package("awtui"), 
            new Package("qtui"), 
            new Package("swingui"), 
            new Package("wxtui")
        };
        
        // Add package dependencies
        for (int i = 0; i < targetPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(SRC_PACKAGE, targetPackages[i]);
        }
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 0; i < targetPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(targetPackages[i]));
        }
    }
    
    @Test
    public void testDependenciesSortedReversedAlphabetically() {
        Package[] targetPackages = {
            new Package("awtui"), 
            new Package("qtui"), 
            new Package("swingui"), 
            new Package("wxtui")
        };
        
        // Add package dependencies
        for (int i = 0; i < targetPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                SRC_PACKAGE, 
                targetPackages[targetPackages.length - i - 1]
            );
        }
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 0; i < targetPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(targetPackages[i]));
        }
    }
    
    @Test
    public void testDependenciesUnsorted() {
        Package[] targetPackages = {
            new Package("swingui"), 
            new Package("qtui"), 
            new Package("awtui"),
            new Package("wxtui")
        };
        
        // Add package dependencies
        for (int i = 0; i < targetPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                SRC_PACKAGE, 
                targetPackages[i]
            );
        }
        
        // Sort the target packages
        Arrays.sort(targetPackages);
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 0; i < targetPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(targetPackages[i]));
        }
    }
    
    @Test
    public void testDirectDependencies() {
        Package[] consideredPackages = {
            new Package("awtui"), 
            new Package("qtui"), 
            new Package("swingui"), 
            new Package("wxtui"),
        };
        
        // Add package dependencies
        for (int i = 0; i < consideredPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                SRC_PACKAGE, 
                consideredPackages[i]
            );
        }
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 0; i < consideredPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(consideredPackages[i]));
        }
    }
    
    @Test
    public void testTransitiveDependencies() {
        Package[] consideredPackages = {
            SRC_PACKAGE,
            new Package("awtui"), 
            new Package("qtui_"), 
            new Package("swinGUI7"), 
            new Package("wXtui"),
        };
        
        // Add package dependencies
        for (int i = 1; i < consideredPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                consideredPackages[i - 1], 
                consideredPackages[i]
            );
        }
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 1; i < consideredPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(consideredPackages[i]));
        }
    }
    
    @Test
    public void testCircularTransitiveDependencies() {
        Package[] consideredPackages = {
            SRC_PACKAGE,
            new Package("awtui"), 
            new Package("qtui"), 
            new Package("swingui"), 
            new Package("wxtui"),
        };
        
        // Add package dependencies
        for (int i = 0; i < consideredPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                consideredPackages[i], 
                consideredPackages[(i + 1) % consideredPackages.length]
            );
        }
        
        // Check package dependencies
        Iterator<Package> srcPackageDependencies = getPackageDependencies(SRC_PACKAGE).iterator();
        
        for (int i = 1; i < consideredPackages.length; ++i) {
            assertTrue(srcPackageDependencies.next().equals(consideredPackages[i]));
        }
    }
    
    @Test
    public void testSamePackageNamesWhenIgnoringCase() {
        Package[] consideredPackages = {
            new Package("awtui"), 
            new Package("aWtui"), 
            new Package("aWTui")
        };
        
        // Add package dependencies
        for (int i = 0; i < consideredPackages.length; ++i) {
            packageDependencies.addNewPackageDependency(
                consideredPackages[i],
                new Package(SRC_PACKAGE.getName() + (i + 1)) 
            );
        }
        
        // Check package dependencies
        Set<Package> srcPackageDependencies = getPackageDependencies(consideredPackages[0]);
        
        assertTrue(srcPackageDependencies.size() == 1);
    }
    
    /**
     * Get the dependencies for a given package.
     * 
     * @param givenPackage The given package.
     * @return The package dependencies.
     */
    private Set<Package> getPackageDependencies(Package givenPackage) {
        return packageDependenciesFinder.find(givenPackage, packageDependencies);
    }
    
}
