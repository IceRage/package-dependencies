package com.bt.coding_test.model;

/**
 * Class for representing a package.
 */
public class Package implements Comparable<Package> {

    private String name; // Package name

    /**
     * Construct a new package with the given name.
     * @param name The name of the package.
     */
    public Package(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int compareTo(Package rhsPackage) {
        return (name.compareTo(rhsPackage.name));
    }
    
    @Override
    public boolean equals(Object rhsPackage) {
        // Two packages are equal if they have the same name
        return (name.equals(((Package) rhsPackage).name));
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @Override
    public String toString() {
        return name;
    }
    
}
