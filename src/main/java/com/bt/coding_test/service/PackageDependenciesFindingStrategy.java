package com.bt.coding_test.service;

import java.util.Set;

import com.bt.coding_test.model.Package;
import com.bt.coding_test.model.PackageDependenciesDirectedGraph;

/**
 * Interface used to define a strategy for finding package dependencies in a package dependencies directed graph.
 */
public interface PackageDependenciesFindingStrategy {

    /**
     * Find all the dependencies of the source package considering the given graph.
     *  
     * @param sourcePackage The source package.
     * @param graph         The package dependencies directed graph.
     * @return The direct and transitive package dependencies of the source package.
     */
    public Set<Package> find(Package sourcePackage, PackageDependenciesDirectedGraph graph);
    
}
