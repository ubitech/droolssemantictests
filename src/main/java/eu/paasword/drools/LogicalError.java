package eu.paasword.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class LogicalError {

private String message;

    public LogicalError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
}
