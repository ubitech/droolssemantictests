package com.javacodegeeks.drools;

import java.util.logging.Logger;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * This is a sample class to launch a rule.
 */
public class DroolsTest {

    private static final Logger logger = Logger.getLogger(DroolsTest.class.getName());    
    
    public static final void main(String[] args) {
        try {
            // load up the knowledge base
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();
            KieSession kSession = kContainer.newKieSession("ksession-rules");

            Clazz area = new Clazz("area",null);
            Clazz subarea = new Clazz("subarea",area);
            Clazz subsubarea = new Clazz("subsubarea",subarea);
            Clazz subsubsubarea = new Clazz("subsubsubarea",area);
            InstanceOfClazz greece = new InstanceOfClazz("greece", area);
            InstanceOfClazz athens = new InstanceOfClazz("athens", subarea);
            InstanceOfClazz filadelfeia = new InstanceOfClazz("filadelfeia", subsubarea);
            
            Clazz requestor = new Clazz("Requestor",null);            
            InstanceOfClazz panagiotis = new InstanceOfClazz("panagiotis",requestor);            
            ObjectProperty requestorhasLocation = new ObjectProperty("hasLocation",requestor,subarea);
            
            KnowledgeTriple k1 = new KnowledgeTriple(panagiotis, requestorhasLocation, athens);
            
            //add knowledge
            kSession.insert(area);
            kSession.insert(subarea);            
            kSession.insert(subsubarea);            
            kSession.insert(subsubsubarea);            
            kSession.insert(greece);            
            kSession.insert(athens);            
            kSession.insert(filadelfeia);       
            kSession.insert(requestor);       
            kSession.insert(panagiotis);       
            kSession.insert(requestorhasLocation);                   
            kSession.insert(k1);                   
            
            //fire
            kSession.fireAllRules();
//            logger.info("I will fire again!");
//            kSession.fireAllRules();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//EoM
        
}//EoC
