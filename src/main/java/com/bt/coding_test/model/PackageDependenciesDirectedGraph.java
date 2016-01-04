package com.bt.coding_test.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Directed graph used to represent package dependencies.
 */
public class PackageDependenciesDirectedGraph {

    private Map<Package, Set<Package>> dependencies;    // The direct dependencies of each package
    
    
    public PackageDependenciesDirectedGraph() {
        dependencies = new HashMap<Package, Set<Package>>();
    }
    
    /**
     * Add a new dependency for a package.
     * 
     * Assumption: The given packages have valid names.
     * 
     * @param sourcePackage        The source package for which the dependency is added.
     * @param packageDependency The package on which the source package depends.
     */
    public void addNewPackageDependency(Package sourcePackage, Package packageDependency) {
        Set<Package> sourcePackageDependencies = dependencies.get(sourcePackage);
        
        // If the source package does not have any dependencies then create and add a new set to the dependencies map
        if (sourcePackageDependencies == null) {
            sourcePackageDependencies = new TreeSet<Package>();
            
            dependencies.put(sourcePackage, sourcePackageDependencies);
        }
        
        sourcePackageDependencies.add(packageDependency);
    }
    
    /**
     * Get an iterator over the direct package dependencies of the given source package.
     *  
     * @param sourcePackage The source package considered.
     * @return An iterator over the direct dependencies of the given source package. 
     */
    public Iterator<Package> getDirectPackageDependencies(Package sourcePackage) {
        if (!dependencies.containsKey(sourcePackage)) {
            return null;
        } else {
            return dependencies.get(sourcePackage).iterator();
        }
    }
    
}
