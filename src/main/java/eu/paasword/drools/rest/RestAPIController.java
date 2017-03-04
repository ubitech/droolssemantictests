package eu.paasword.drools.rest;

import eu.paasword.drools.service.RulesEngineService;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/*
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
@RestController
@RequestMapping("/api/semanticpolicyengine")
public class RestAPIController {

    private static final Logger logger = Logger.getLogger(RestAPIController.class.getName());
    
    @Autowired
    RulesEngineService res;
    
    @RequestMapping(method = RequestMethod.GET)
    public String test() {
        logger.info("Rest Test Request");
        return "echo";
    }

    @RequestMapping(value = "/loadontology", method = RequestMethod.GET)    
    public String loadOntology() {
        logger.info("REST - loadOntology");
        res.handleRequest();
        return "handleRequest";
    }        
    
    @RequestMapping(value = "/handlerequest", method = RequestMethod.GET)    
    public String handleRequest() {
        logger.info("REST - handleRequest");
        res.handleRequest();
        return "handleRequest";
    }    
    
}//EoC
