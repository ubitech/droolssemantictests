package eu.paasword.drools.rest.request;

import java.util.List;

/**
 *
 * @author ubuntu
 */
public class AuthorizationRequest {
    
    // Request Info that will be utilized to enrich the working memory (through the creation of IoCs and KTs )
    
    private String requestid;
    private String subjectinstance;
    private String objectinstance;
    private String actioninstance;
    private List<Handler>  handlers;
            
    // Additional elements that may be used by general purpose handlers
    
    private String requestorip;
    
    //Parameters that will affect the session.fireAll( FILTER )
    //CONVENTION only one of the items below should be filled
    //They should be filled using a top down prioritization
    
    private PolicySet policyset;      
    private Policy policy;      
    private Rule rule;              

    public AuthorizationRequest() {
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getSubjectinstance() {
        return subjectinstance;
    }

    public void setSubjectinstance(String subjectinstance) {
        this.subjectinstance = subjectinstance;
    }

    public String getObjectinstance() {
        return objectinstance;
    }

    public void setObjectinstance(String objectinstance) {
        this.objectinstance = objectinstance;
    }

    public String getActioninstance() {
        return actioninstance;
    }

    public void setActioninstance(String actioninstance) {
        this.actioninstance = actioninstance;
    }

    public String getRequestorip() {
        return requestorip;
    }

    public void setRequestorip(String requestorip) {
        this.requestorip = requestorip;
    }

    public PolicySet getPolicyset() {
        return policyset;
    }

    public void setPolicyset(PolicySet policyset) {
        this.policyset = policyset;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
       
}
