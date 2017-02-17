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
            //Classes
            Clazz area = new Clazz("area",null);
            Clazz subarea = new Clazz("subarea",area);
            Clazz subsubarea = new Clazz("subsubarea",subarea);
            Clazz requestor = new Clazz("Requestor",null);            
            Clazz ipaddress = new Clazz("IP Address",null);            
            Clazz blacklistedipaddress = new Clazz("Blacklisted IP Address",ipaddress);            
            //instances            
            InstanceOfClazz greece = new InstanceOfClazz("greece", area);
            InstanceOfClazz athens = new InstanceOfClazz("athens", subarea);
            InstanceOfClazz filadelfeia = new InstanceOfClazz("filadelfeia", subsubarea);           
            InstanceOfClazz panagiotis = new InstanceOfClazz("panagiotis",requestor);    
            InstanceOfClazz localhost = new InstanceOfClazz("127.0.0.1",ipaddress);    
            InstanceOfClazz iccsip = new InstanceOfClazz("147.102.23.1",ipaddress);    
            InstanceOfClazz ubiip = new InstanceOfClazz("192.168.1.1",blacklistedipaddress);    
            //object properties
            ObjectProperty requestorhasSubarea = new ObjectProperty("hasSubarea",requestor,subarea);
            ObjectProperty requestorhasIPAddress = new ObjectProperty("hasIPAddress",requestor,ipaddress);
            
            //triples
            KnowledgeTriple t1 = new KnowledgeTriple(panagiotis, requestorhasSubarea, athens);
            KnowledgeTriple t2 = new KnowledgeTriple(panagiotis, requestorhasIPAddress, ubiip);
            
            //add Model to Production Memory
            //classes
            kSession.insert(area);
            kSession.insert(subarea);            
            kSession.insert(subsubarea);            
            kSession.insert(requestor);              
            kSession.insert(ipaddress);      
            kSession.insert(blacklistedipaddress);      
            //instances
            kSession.insert(greece);            
            kSession.insert(athens);            
            kSession.insert(filadelfeia);            
            kSession.insert(panagiotis);       
            kSession.insert(localhost);       
            kSession.insert(iccsip);       
            kSession.insert(ubiip);       
            //object properties
            kSession.insert(requestorhasSubarea);
            kSession.insert(requestorhasIPAddress);
            //triples
            kSession.insert(t1);                   
            kSession.insert(t2);                   
            
            //fire
            kSession.fireAllRules();
            logger.info("---> I will fire again!");
            kSession.fireAllRules();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//EoM
        
}//EoC
