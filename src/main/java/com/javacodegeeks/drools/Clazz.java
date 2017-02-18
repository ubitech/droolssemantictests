package com.javacodegeeks.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class Clazz {
       
    private String name;    
    private Clazz parent;

    public Clazz(String name) {
        this.name = name;
        this.parent = null;
    }    
    
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

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Clazz other = (Clazz) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.parent != other.parent && (this.parent == null || !this.parent.equals(other.parent))) {
            return false;
        }
        return true;
    }
        
    
    
}
