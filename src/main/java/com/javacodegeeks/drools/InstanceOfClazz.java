package com.javacodegeeks.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class InstanceOfClazz {
    
    private String name;     
    private Clazz clazz;     

    public InstanceOfClazz(String name, Clazz clazz) {
        this.name = name;
        this.clazz = clazz;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }    

    @Override
    public String toString() {
        return "InstanceOfClazz{" + "name=" + name + ", clazz=" + clazz + '}';
    }        
    
}
