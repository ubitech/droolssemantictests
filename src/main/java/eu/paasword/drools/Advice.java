
package eu.paasword.drools;

/**
 *
 * @author ubuntu
 */
public class Advice {
    
    private String advice; 
    private String requestid;

    public Advice(String advice, String requestid) {
        this.advice = advice;
        this.requestid = requestid;
    }    
    
    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }    
    
}
