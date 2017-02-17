package com.javacodegeeks.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class KnowledgeTriple {
    
    InstanceOfClazz subject;
    ObjectProperty predicate;
    InstanceOfClazz object;    

    public KnowledgeTriple(InstanceOfClazz subject, ObjectProperty predicate, InstanceOfClazz object) {
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }    
    
    public InstanceOfClazz getSubject() {
        return subject;
    }

    public void setSubject(InstanceOfClazz subject) {
        this.subject = subject;
    }

    public ObjectProperty getPredicate() {
        return predicate;
    }

    public void setPredicate(ObjectProperty predicate) {
        this.predicate = predicate;
    }

    public InstanceOfClazz getObject() {
        return object;
    }

    public void setObject(InstanceOfClazz object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "KnowledgeTriple{" + "subject=" + subject + ", predicate=" + predicate + ", object=" + object + '}';
    }    
    
}
