package com.javacodegeeks.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class Clazz {
       
    private String name;    
    private Clazz parent;

    public Clazz(String name, Clazz parent) {
        this.name = name;
        this.parent = parent;
    }    
           
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Clazz getParent() {
        return parent;
    }

    public void setParent(Clazz parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Clazz{" + "name=" + name + ", parent=" + parent + '}';
    }
        
    
    
}
