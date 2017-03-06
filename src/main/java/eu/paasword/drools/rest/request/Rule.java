package eu.paasword.drools.rest.request;

public class Rule {

    private String ruleidentifier;          //Will define the name of the rule  in the FILTER 

    public Rule(String ruleidentifier) {
        this.ruleidentifier = ruleidentifier;
    }    
    
    public String getRuleidentifier() {
        return ruleidentifier;
    }

    public void setRuleidentifier(String ruleidentifier) {
        this.ruleidentifier = ruleidentifier;
    }    
    
}
