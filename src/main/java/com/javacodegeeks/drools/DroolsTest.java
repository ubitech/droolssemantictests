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
            Clazz area = new Clazz("Area",null);
            Clazz continent = new Clazz("Continent",area);
            Clazz country = new Clazz("Country",area);
            Clazz city = new Clazz("City",area);
            Clazz region = new Clazz("Region",area);
            
            Clazz requestor = new Clazz("Requestor",null);            
            Clazz developer = new Clazz("Developer",requestor);            
            Clazz ipaddress = new Clazz("IP Address",null);            
            Clazz blacklistedipaddress = new Clazz("Blacklisted IP Address",ipaddress);            
            //instances            
            InstanceOfClazz africa = new InstanceOfClazz("Africa", continent);
            InstanceOfClazz europe = new InstanceOfClazz("Europe", continent);
            InstanceOfClazz greece = new InstanceOfClazz("Greece", country);
            InstanceOfClazz italy = new InstanceOfClazz("Italy", country);
            InstanceOfClazz athens = new InstanceOfClazz("Athens", city);
            InstanceOfClazz volos = new InstanceOfClazz("Volos", city);
            InstanceOfClazz milan = new InstanceOfClazz("Milan", city);
            InstanceOfClazz rome = new InstanceOfClazz("Rome", city);
            InstanceOfClazz filadelfeia = new InstanceOfClazz("filadelfeia", region);           
            InstanceOfClazz reqinstance = new InstanceOfClazz("reqinstance",requestor);    
            InstanceOfClazz devinstance = new InstanceOfClazz("devinstance",developer);    
            InstanceOfClazz localhost = new InstanceOfClazz("127.0.0.1",ipaddress);    
            InstanceOfClazz iccsip = new InstanceOfClazz("147.102.23.1",ipaddress);    
            InstanceOfClazz ubiip = new InstanceOfClazz("192.168.1.1",blacklistedipaddress);    

            //object properties
            ObjectProperty isLocatedAt = new ObjectProperty("isLocatedAt",area,area,true);
//            ObjectProperty requestorhasSubarea = new ObjectProperty("isLocatedAt",region,city);            
//            ObjectProperty requestorhasIPAddress = new ObjectProperty("hasIPAddress",requestor,ipaddress);
            
            //triples
//            KnowledgeTriple t1 = new KnowledgeTriple(panagiotis, requestorhasSubarea, athens);
//            KnowledgeTriple t2 = new KnowledgeTriple(devinstance, requestorhasIPAddress, ubiip);
//            KnowledgeTriple t3 = new KnowledgeTriple(greece, isLocatedAt, europe);
            KnowledgeTriple t4 = new KnowledgeTriple(filadelfeia, isLocatedAt, athens);
            KnowledgeTriple t5 = new KnowledgeTriple(athens, isLocatedAt, greece);
            
            //add Model to Production Memory
            //classes
            kSession.insert(continent);
            kSession.insert(country);
            kSession.insert(city);            
            kSession.insert(region);            
            kSession.insert(requestor);              
            kSession.insert(developer);
            kSession.insert(ipaddress);      
            kSession.insert(blacklistedipaddress);      
            //instances            
            kSession.insert(africa);            
            kSession.insert(europe);            
            kSession.insert(greece);            
            kSession.insert(italy);            
            kSession.insert(athens);            
            kSession.insert(volos);            
            kSession.insert(milan);            
            kSession.insert(rome);            
            kSession.insert(filadelfeia);            
            kSession.insert(reqinstance);       
            kSession.insert(devinstance);       
            kSession.insert(localhost);       
            kSession.insert(iccsip);       
            kSession.insert(ubiip);       
            //object properties
            kSession.insert(isLocatedAt);
//            kSession.insert(requestorhasSubarea);
//            kSession.insert(requestorhasIPAddress);
            
            //triples
//            kSession.insert(t1);                   
//            kSession.insert(t2);                   
//            kSession.insert(t3);                   
            kSession.insert(t4);                   
            kSession.insert(t5);                   
            
            //fire
            kSession.fireAllRules();
            logger.info("---> I will fire again!");
            kSession.fireAllRules();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//EoM
        
}//EoC
