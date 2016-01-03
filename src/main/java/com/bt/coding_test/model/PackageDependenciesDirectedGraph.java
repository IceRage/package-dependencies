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
    public Iterator<Package> getDirectPackageDependenciesIterator(Package sourcePackage) {
        if (!dependencies.containsKey(sourcePackage)) {
            return null;
        } else {
            return dependencies.get(sourcePackage).iterator();
        }
    }
    
    /**
     * Get the direct and transitive package dependencies of the given source package.
     * 
     * @param sourcePackage The source package considered.
     * @return The direct and transitive package dependencies of the given source package.
     */
    public Set<Package> getAllPackageDependencies(Package sourcePackage) {
        Set<Package>          allPackageDependencies  = new TreeSet<Package>();
        Map<Package, Boolean> consideredPackages      = new HashMap<Package, Boolean>();

        // Mark the source package as considered
        consideredPackages.put(sourcePackage, true);
        
        computeAllPackageDependencies(sourcePackage, allPackageDependencies, consideredPackages);
        
        return allPackageDependencies;
    }

    /**
     * Compute all the package dependencies for the given source package.
     * This method implements a version of the Depth First Search (DFS) algorithm executed 
     * for a single (source) vertex of a directed graph.
     * 
     * @param sourcePackage                The source package considered.
     * @param allPackageDependencies    All the package dependencies for the given source package.
     * @param consideredPackages        The packages considered so far.
     */
    private void computeAllPackageDependencies(Package sourcePackage, 
                                               Set<Package> allPackageDependencies,
                                               Map<Package, Boolean> consideredPackages) {
        Set<Package> sourcePackageDependencies = dependencies.get(sourcePackage);
        
        if (sourcePackageDependencies != null) {
            for (Package packageDependency : sourcePackageDependencies) {
                if (!consideredPackages.containsKey(packageDependency)) {
                    // Mark package dependency as visited
                    consideredPackages.put(packageDependency, true);
                    
                    allPackageDependencies.add(packageDependency);
                    
                    computeAllPackageDependencies(packageDependency, allPackageDependencies, consideredPackages);
                }
            }
        }
    }
    
}
