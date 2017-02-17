package com.javacodegeeks.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class ObjectProperty {

    private String name;
    private Clazz domain;
    private Clazz range;
    private ObjectProperty parent;
    
    public ObjectProperty(String name, Clazz domain, Clazz range) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.parent = null;
    }    

    public ObjectProperty(String name, Clazz domain, Clazz range, ObjectProperty parent) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.parent = parent;
    }   
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Clazz getDomain() {
        return domain;
    }

    public void setDomain(Clazz domain) {
        this.domain = domain;
    }

    public Clazz getRange() {
        return range;
    }

    public void setRange(Clazz range) {
        this.range = range;
    }

    public ObjectProperty getParent() {
        return parent;
    }

    public void setParent(ObjectProperty parent) {
        this.parent = parent;
    }        

    @Override
    public String toString() {
        return "ObjectProperty{" + "name=" + name + ", domain=" + domain + ", range=" + range + ", parent=" + parent + '}';
    }
    
}
