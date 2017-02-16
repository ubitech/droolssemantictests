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
            InstanceOfClazz instanceofclass1 = new InstanceOfClazz("greece", area);
            InstanceOfClazz instanceofclass2 = new InstanceOfClazz("athens", subarea);
            
            //add knowledge
            kSession.insert(area);
            kSession.insert(subarea);            
            kSession.insert(instanceofclass1);            
            kSession.insert(instanceofclass2);            
            //fire
            kSession.fireAllRules();
            logger.info("I will fire again!");
            kSession.fireAllRules();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//EoM
        
}//EoC
