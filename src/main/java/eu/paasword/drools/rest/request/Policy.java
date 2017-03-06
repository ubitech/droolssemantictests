package eu.paasword.drools.rest.request;

import java.util.List;

/**
 *
 * @author Spyros
 */
public class Policy {
    
    private String policyidentifier;             
    private String policycombiningalgorithm;     //Available options are :  XXX
    private List<Rule> rules;                    //Rule objects        

    public Policy(String policyidentifier, String policycombiningalgorithm, List<Rule> rules) {
        this.policyidentifier = policyidentifier;
        this.policycombiningalgorithm = policycombiningalgorithm;
        this.rules = rules;
    }

    public String getPolicyidentifier() {
        return policyidentifier;
    }

    public void setPolicyidentifier(String policyidentifier) {
        this.policyidentifier = policyidentifier;
    }

    public String getPolicycombiningalgorithm() {
        return policycombiningalgorithm;
    }

    public void setPolicycombiningalgorithm(String policycombiningalgorithm) {
        this.policycombiningalgorithm = policycombiningalgorithm;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }        
    
}
