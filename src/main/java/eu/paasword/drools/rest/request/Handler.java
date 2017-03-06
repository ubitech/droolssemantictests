package eu.paasword.drools.rest.request;

public class Handler {

    private String handleridentifier;
    private String restendpoint;
    
    private String domainclazzname;
    private String domainargumentinstance;
    private String rangeclazzname;   //

    public Handler() {
    }

    public String getHandleridentifier() {
        return handleridentifier;
    }

    public void setHandleridentifier(String handleridentifier) {
        this.handleridentifier = handleridentifier;
    }

    public String getRestendpoint() {
        return restendpoint;
    }

    public void setRestendpoint(String restendpoint) {
        this.restendpoint = restendpoint;
    }

    public String getDomainclazzname() {
        return domainclazzname;
    }

    public void setDomainclazzname(String domainclazzname) {
        this.domainclazzname = domainclazzname;
    }

    public String getDomainargumentinstance() {
        return domainargumentinstance;
    }

    public void setDomainargumentinstance(String domainargumentinstance) {
        this.domainargumentinstance = domainargumentinstance;
    }

    public String getRangeclazzname() {
        return rangeclazzname;
    }

    public void setRangeclazzname(String rangeclazzname) {
        this.rangeclazzname = rangeclazzname;
    }

    
    
    
    
}
