package eu.paasword.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class ObjectProperty {

    private String name;
    private Clazz domain;
    private Clazz range;
    private boolean transitive;
    private ObjectProperty parent;
    
    public ObjectProperty(String name, Clazz domain, Clazz range) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.parent = null;
        this.transitive = false;
    }    

    public ObjectProperty(String name, Clazz domain, Clazz range, ObjectProperty parent) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.parent = parent;
        this.transitive = false;
    }   

    public ObjectProperty(String name, Clazz domain, Clazz range, boolean transitive) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.transitive = transitive;
    }
    
    public ObjectProperty(String name, Clazz domain, Clazz range, boolean transitive, ObjectProperty parent) {
        this.name = name;
        this.domain = domain;
        this.range = range;
        this.transitive = transitive;
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

    public boolean isTransitive() {
        return transitive;
    }

    public void setTransitive(boolean transitive) {
        this.transitive = transitive;
    }    

    @Override
    public String toString() {
        return "ObjectProperty{" + "name=" + name + ", domain=" + domain + ", range=" + range + ", transitive=" + transitive + ", parent=" + parent + '}';
    }   
    
}
