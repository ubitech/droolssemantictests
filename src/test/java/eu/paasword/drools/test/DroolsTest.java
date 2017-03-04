package eu.paasword.drools.test;

import eu.paasword.drools.Clazz;
import eu.paasword.drools.InstanceOfClazz;
import eu.paasword.drools.KnowledgeTriple;
import eu.paasword.drools.ObjectProperty;
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
            Clazz area = new Clazz("Area", null);
            Clazz continent = new Clazz("Continent", area);     //sub-classing will trigger "Class Transitiveness Inference"
            Clazz country = new Clazz("Country", area);
            Clazz city = new Clazz("City", area);
            Clazz region = new Clazz("Region", area);
            
            Clazz ipaddress = new Clazz("IP Address", null);
            Clazz privateipaddress = new Clazz("Private IP Address", ipaddress);
            Clazz publicipaddress = new Clazz("Public IP Address", ipaddress);            
            Clazz ipclassificationstatus = new Clazz("IP Classification Status", null);
            
            Clazz building = new Clazz("Building", null);
            Clazz partofbuilding = new Clazz("Part Of Building", null);
            Clazz floor = new Clazz("Floor", partofbuilding);
            Clazz datacenter = new Clazz("Datacenter", partofbuilding);
            
            Clazz employee = new Clazz("Employee", null);
            Clazz secretery = new Clazz("Secretery", employee);            
            Clazz developer = new Clazz("Developer", employee);            
            
            Clazz request = new Clazz("Request", null);            
            Clazz timestamp = new Clazz("Timestamp", null);            
            
            //instances            
            InstanceOfClazz africa = new InstanceOfClazz("Africa", continent);
            InstanceOfClazz europe = new InstanceOfClazz("Europe", continent);
            InstanceOfClazz greece = new InstanceOfClazz("Greece", country);    //instance of subclass will trigger "Supertype Inheritance Inference"
            InstanceOfClazz italy = new InstanceOfClazz("Italy", country);
            InstanceOfClazz athens = new InstanceOfClazz("Athens", city);
            InstanceOfClazz volos = new InstanceOfClazz("Volos", city);
            InstanceOfClazz milan = new InstanceOfClazz("Milan", city);
            InstanceOfClazz rome = new InstanceOfClazz("Rome", city);            
            InstanceOfClazz filadelfeia = new InstanceOfClazz("Filadelfeia", region);
            InstanceOfClazz chalandri = new InstanceOfClazz("Chalandri", region);   
            

            InstanceOfClazz localhost = new InstanceOfClazz("127.0.0.1", privateipaddress);
            InstanceOfClazz iccsip = new InstanceOfClazz("147.102.23.1", publicipaddress);
            InstanceOfClazz ubiip = new InstanceOfClazz("213.249.38.66", publicipaddress);
            InstanceOfClazz anypublicip = new InstanceOfClazz("any", publicipaddress);            
            InstanceOfClazz whitelisted = new InstanceOfClazz("Whitelisted", ipclassificationstatus);
            InstanceOfClazz blacklisted = new InstanceOfClazz("Blacklisted", ipclassificationstatus);
            
            InstanceOfClazz ubitechbuilding = new InstanceOfClazz("Ubitech Building", building);
            InstanceOfClazz ubifirstfloor = new InstanceOfClazz("Ubitech 1st Floor", floor);
            InstanceOfClazz ubisecondfloor = new InstanceOfClazz("Ubitech 2nd Floor", floor);
            InstanceOfClazz ubidatacenter = new InstanceOfClazz("Ubitech Datacenter", datacenter);
                        
            InstanceOfClazz pgouvas = new InstanceOfClazz("Panagiotis Gouvas", developer);
            InstanceOfClazz jokriv = new InstanceOfClazz("Ioanna Krivitsaki", secretery);
            
            //this created by a request processor
            InstanceOfClazz testrequest = new InstanceOfClazz("request1", request);     //Request@20/2/2017-By-pgouvas
            InstanceOfClazz testtimestamp = new InstanceOfClazz("10", timestamp);
            
            
            //Object Properties
            ObjectProperty areaContainsArea = new ObjectProperty("Area_contains_Area", area, area, true);         //transitive property
            ObjectProperty EmployeehasAccessToBuilding = new ObjectProperty("Employee_hasAccessTo_Building", employee, building);
            ObjectProperty developerHasAccessToFloor_error = new ObjectProperty("Developer_hasAccessTo_Floor_error", developer, floor, EmployeehasAccessToBuilding);    //This will trigger the rule "Consistency Checking of Sub-Property Definition based on Range Restrictions" since floor is not a building
            ObjectProperty developerHasAccessToBuilding = new ObjectProperty("Developer_hasAccessTo_Floor", developer, building, EmployeehasAccessToBuilding);    //This will trigger the rule "Consistency Checking of Sub-Property Definition based on Range Restrictions" since floor is not a building
            ObjectProperty developerHasSecretery = new ObjectProperty("Developer_has_Secretery", developer, secretery);    
            
            ObjectProperty requestHasTimestamp = new ObjectProperty("Request_hasTimestamp_Timestamp", request, timestamp);   
            ObjectProperty requestComesfromEmployee = new ObjectProperty("Request_comesFrom_Employee", request, employee);   
            ObjectProperty requestHasLocationArea = new ObjectProperty("Request_hasLocation_Area", request, area);               
            ObjectProperty requestHasIP = new ObjectProperty("Request_hasIP_IPAddress", request, ipaddress);   
            ObjectProperty ipHasClassificationstatus = new ObjectProperty("IPAddress_hasStatus_Classificationstatus", ipaddress, ipclassificationstatus);            
            
            //Knowledge Triples
            KnowledgeTriple t1_error = new KnowledgeTriple(localhost, areaContainsArea, athens);      //This will trigger the rule "Consistency Checking of Knowledge Triples based on Domain Restrictions" since ip is not an Area
            KnowledgeTriple t2_error = new KnowledgeTriple(europe, areaContainsArea, localhost);      //This will trigger the rule "Consistency Checking of Knowledge Triples based on Range Restrictions" since ip is not an Area
            KnowledgeTriple t3 = new KnowledgeTriple(europe, areaContainsArea, greece);     
            KnowledgeTriple t4 = new KnowledgeTriple(greece, areaContainsArea, athens);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"
            KnowledgeTriple t5 = new KnowledgeTriple(pgouvas, EmployeehasAccessToBuilding, ubitechbuilding);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"

                //request
            KnowledgeTriple r1 = new KnowledgeTriple(testrequest, requestHasTimestamp, testtimestamp);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"
            KnowledgeTriple r2 = new KnowledgeTriple(testrequest, requestComesfromEmployee, pgouvas);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"
            KnowledgeTriple r3 = new KnowledgeTriple(testrequest, requestHasIP, ubiip);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"
            KnowledgeTriple r4 = new KnowledgeTriple(ubiip, ipHasClassificationstatus, whitelisted);         //t3 and t4 will trigger the rule "Knowledge Expansion through Transitive Property Interpretation"

            
            //add Model to Production Memory
            //classes            
            kSession.insert(area);
            kSession.insert(continent);
            kSession.insert(country);
            kSession.insert(city);
            kSession.insert(region);
            
            kSession.insert(ipaddress);
            kSession.insert(privateipaddress);
            kSession.insert(publicipaddress);
            kSession.insert(ipclassificationstatus);
            
            kSession.insert(building);
            kSession.insert(partofbuilding);
            kSession.insert(floor);
            kSession.insert(datacenter);
            
            kSession.insert(employee);
            kSession.insert(secretery);
            kSession.insert(developer);
            
            kSession.insert(request);
            kSession.insert(timestamp);
            
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
            kSession.insert(chalandri);
            
            kSession.insert(localhost);
            kSession.insert(iccsip);
            kSession.insert(ubiip);
            kSession.insert(anypublicip);
            kSession.insert(whitelisted);
            kSession.insert(blacklisted);
            
            kSession.insert(ubitechbuilding);                        
            kSession.insert(ubifirstfloor);                        
            kSession.insert(ubisecondfloor);                        
            kSession.insert(ubidatacenter);
            
            kSession.insert(pgouvas);                        
            kSession.insert(jokriv);
            
            kSession.insert(testrequest);                        
            kSession.insert(testtimestamp);                        
            
            //Object Properties 
            kSession.insert(areaContainsArea);
            kSession.insert(EmployeehasAccessToBuilding);
            kSession.insert(developerHasAccessToFloor_error);
            kSession.insert(developerHasAccessToBuilding);
            kSession.insert(developerHasSecretery);
            
            kSession.insert(requestHasTimestamp);                        
            kSession.insert(requestComesfromEmployee);                        
            kSession.insert(requestHasLocationArea);                        
            kSession.insert(requestHasIP);                        
            kSession.insert(ipHasClassificationstatus);                        
            
            //Knowledge Triples
            kSession.insert(t1_error);
            kSession.insert(t2_error);
            kSession.insert(t3);
            kSession.insert(t4);
            kSession.insert(t5);
            
            kSession.insert(r1);
            kSession.insert(r2);
            kSession.insert(r3);
            kSession.insert(r4);

            //fire Rules
            kSession.fireAllRules();
//            logger.info("---> I will fire again!");
//            kSession.fireAllRules();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//EoM

}//EoC
