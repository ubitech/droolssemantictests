package eu.paasword.drools.rest.request;

import java.util.List;

/**
 *
 * @author ubuntu
 */
public class AuthorizationResponse {
    
    // Request Info that will be utilized to enrich the working memory (through the creation of IoCs and KTs )
    
    private String requestid;
    private String advice;

    public AuthorizationResponse() {
    }

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }
    
}
