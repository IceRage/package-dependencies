package com.bt.coding_test.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;

/**
 * Strategy class using depth-first search to find package dependencies in a package dependencies directed graph.
 */
public class DFSPackageDependenciesFindingStrategy implements PackageDependenciesFindingStrategy {

    /**
     * Find the direct and transitive package dependencies of the given source package.
     * 
     * @param sourcePackage The source package considered.
     * @param graph         The considered package dependencies directed graph.
     * @return The direct and transitive package dependencies of the given source package.
     */
    @Override
    public Set<Package> find(Package sourcePackage, PackageDependenciesDirectedGraph graph) {
        Set<Package>          allPackageDependencies  = new TreeSet<Package>();
        Map<Package, Boolean> consideredPackages      = new HashMap<Package, Boolean>();

        // Mark the source package as considered
        consideredPackages.put(sourcePackage, true);
        
        computeAllPackageDependencies(sourcePackage, graph, allPackageDependencies, consideredPackages);
        
        return allPackageDependencies;
    }

    /**
     * Compute all the package dependencies for the given source package.
     * This method implements a version of the Depth First Search (DFS) algorithm executed 
     * for a single (source) vertex of a directed graph.
     * 
     * @param sourcePackage             The source package considered.
     * @param graph                     The considered package dependencies directed graph.
     * @param allPackageDependencies    All the package dependencies for the given source package.
     * @param consideredPackages        The packages considered so far.
     */
    private void computeAllPackageDependencies(Package sourcePackage,
                                               PackageDependenciesDirectedGraph graph,
                                               Set<Package> allPackageDependencies,
                                               Map<Package, Boolean> consideredPackages) {
        Iterator<Package> sourcePackageDependencies = graph.getDirectPackageDependencies(sourcePackage);
        
        if (sourcePackageDependencies != null) {
            while (sourcePackageDependencies.hasNext()) {
                Package packageDependency = sourcePackageDependencies.next();
                
                if (!consideredPackages.containsKey(packageDependency)) {
                    // Mark package dependency as visited
                    consideredPackages.put(packageDependency, true);
                    
                    allPackageDependencies.add(packageDependency);
                    
                    computeAllPackageDependencies(packageDependency, graph, allPackageDependencies, consideredPackages);
                }
            }
        }
    }
    
}
