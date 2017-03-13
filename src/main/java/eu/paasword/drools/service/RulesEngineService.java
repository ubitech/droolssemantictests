package eu.paasword.drools.service;

import eu.paasword.drools.Clazz;
import eu.paasword.drools.InstanceOfClazz;
import eu.paasword.drools.LogicalError;
import static eu.paasword.drools.config.Initializer.KIE_BASE_MODEL_PREFIX;
import static eu.paasword.drools.config.Initializer.KNOWLEDGEBASE_PREFIX;
import static eu.paasword.drools.config.Initializer.PACKAGE_NAME;
import static eu.paasword.drools.config.Initializer.RULESPACKAGE;
import static eu.paasword.drools.config.Initializer.SESSION_PREFIX;
import eu.paasword.drools.util.ReflectionUtil;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.Message;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.ObjectFilter;
import org.kie.api.runtime.rule.AgendaFilter;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.runtime.rule.Match;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
@Service
public class RulesEngineService {

    private static final Logger logger = Logger.getLogger(RulesEngineService.class.getName());

    private final ReleaseId releaseId = KieServices.Factory.get().newReleaseId(PACKAGE_NAME, "expert-system", "1.0");
    private final KieServices kieServices;
    private final KieFileSystem kieFileSystem;
    private final KieModuleModel kieModuleModel;
    private KieSession ksession;
    private KieContainer kieContainer;

    @Value("${ontology.path}")
    private String ontologypath;
    @Value("${rules.path}")
    private String rulespath;

    @Autowired
    public RulesEngineService() {
        logger.info("Rule Engine initializing...");
        this.kieServices = KieServices.Factory.get();
        this.kieFileSystem = kieServices.newKieFileSystem();
        this.kieModuleModel = kieServices.newKieModuleModel();
    }//EoC    

    public void createKnowledgebase() {
        String knowledgebasename = KNOWLEDGEBASE_PREFIX + "default";  //runningapplication.getId()
        logger.info("Knowledge-base name: " + knowledgebasename);
        KieBaseModel kbase = kieModuleModel.newKieBaseModel(KIE_BASE_MODEL_PREFIX + knowledgebasename).setDefault(true); //.setEventProcessingMode(EventProcessingOption.STREAM);
        kbase.addPackage(RULESPACKAGE + "." + knowledgebasename);
        kbase.setEventProcessingMode(EventProcessingOption.CLOUD);
        String factSessionName = SESSION_PREFIX + knowledgebasename;
        KieSessionModel ksessionmodel = kbase.newKieSessionModel(factSessionName);                                                                       //.setClockType(ClockTypeOption.get("realtime"));
        ksessionmodel.setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL);
        //.setFileLogger("drools.log", 10, true)
//        .setConsoleLogger("logger");

    }//EoM    

    public KieContainer launchKieContainer() {
        Double newversion = Double.parseDouble(releaseId.getVersion()) + 0.1;
        ReleaseId newReleaseId = kieServices.newReleaseId(PACKAGE_NAME, "expert-system", newversion.toString());
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);

        kieFileSystem.generateAndWritePomXML(newReleaseId);
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        logger.log(java.util.logging.Level.INFO, "kieModuleModel--ToXML\n{0}", kieModuleModel.toXML());
        //adding rules
        addRules();
        logger.info("kieBuilder.buildAll()");
        kieBuilder.buildAll();
        //
        if (kieBuilder.getResults().hasMessages(Message.Level.ERROR)) {
            throw new RuntimeException("Build Errors:\n" + kieBuilder.getResults().toString());
        }//if
        logger.info("Spawning new Container");
        kieContainer = kieServices.newKieContainer(newReleaseId);

        String knowledgebasename = KNOWLEDGEBASE_PREFIX + "default";          //knowledgebaseid
        String factSessionName = SESSION_PREFIX + knowledgebasename;
        KieSession kieSession = kieContainer.newKieSession(factSessionName);
        setSession(kieSession);

        return kieContainer;
    }//ΕοΜ    

    public void loadOntology() {
        Map<String, Object> intiorphanclassobjectmap = new HashMap<>();
        Map<String, Object> intermediateclassobjectmap = new HashMap<>();
        Map<String, Object> finalclassobjectmap = new HashMap<>();
        Map<String, Object> finalinstanceobjectmap = new HashMap<>();
        Map<String, Object> initopobjectmap = new HashMap<>();
        Map<String, Object> intermediateopobjectmap = new HashMap<>();
        Map<String, Object> finalopobjectmap = new HashMap<>();
        List<Object> finalktriplesobjectlist = new ArrayList<>();

        try {
            /*
            *   LOAD CLASSES
             */
            //Fetch all Classes that are orphan
            Stream<String> stream = Files.lines(Paths.get(ontologypath));
            intiorphanclassobjectmap = stream
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("C") || (line.split(",")[0]).trim().equalsIgnoreCase("Class"))) //&& (line.split(",")[2]).trim().equalsIgnoreCase("null")
                    .map(line -> ReflectionUtil.createOrphanClazz(ReflectionUtil.getClassLabelFromLine(line)))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));
            final Map<String, Object> temp1 = intiorphanclassobjectmap;
//            logger.info("1st Pass of Classes: ");
//            intiorphanclassobjectmap.values().forEach(System.out::println);

            //Handle Non Orphan
            Stream<String> stream2 = Files.lines(Paths.get(ontologypath));
            intermediateclassobjectmap = stream2
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("C") || (line.split(",")[0]).trim().equalsIgnoreCase("Class")) && !(line.split(",")[2]).trim().equalsIgnoreCase("null"))
                    .map(line -> ReflectionUtil.setParentToClazzObject(temp1.get(ReflectionUtil.getClassLabelFromLine(line)), temp1.get(ReflectionUtil.getParentalClassLabelFromLine(line))))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));

            final Map<String, Object> temp2 = intermediateclassobjectmap;
//            logger.info("Intermediate Pass of Classes: ");
//            intermediateclassobjectmap.values().forEach(System.out::println);

            //Create Final list
            finalclassobjectmap = temp1.values().stream()
                    .filter(clazz -> !temp2.containsKey(ReflectionUtil.getNameOfObject(clazz)))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));
            //append non orphan
            for (Object object : intermediateclassobjectmap.values()) {
                finalclassobjectmap.put(ReflectionUtil.getNameOfObject(object), object);
            }
            final Map<String, Object> temp3 = finalclassobjectmap;              //FINAL CLASSES
//            logger.info("\nClasses: ");
//            finalclassobjectmap.values().forEach(System.out::println);

            /*
            *   LOAD CLASS INSTANCES
             */
            Stream<String> stream3 = Files.lines(Paths.get(ontologypath));
            finalinstanceobjectmap = stream3
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("IoC") || (line.split(",")[0]).trim().equalsIgnoreCase("InstanceOfClass"))) //&& (line.split(",")[2]).trim().equalsIgnoreCase("null")
                    .map(line -> ReflectionUtil.createInstanceOfClazz(ReflectionUtil.getInstanceLabelFromLine(line), temp3.get(ReflectionUtil.getInstanceClassLabelFromLine(line))))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));

            final Map<String, Object> temp4 = finalinstanceobjectmap;              //FINAL IOC            

//            logger.info("\nInstances Of Classes: ");
//            finalinstanceobjectmap.values().forEach(System.out::println);

            /*
            *   LOAD OBJECT PROPERTIES
             */
            Stream<String> stream4 = Files.lines(Paths.get(ontologypath));
            initopobjectmap = stream4
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("OP") || (line.split(",")[0]).trim().equalsIgnoreCase("ObjectProperty"))) //&& (line.split(",")[2]).trim().equalsIgnoreCase("null")
                    .map(line -> ReflectionUtil.createOrphanObjectProperty(ReflectionUtil.getOPLabelFromLine(line), temp3.get(ReflectionUtil.getDomainOPLabelFromLine(line)), temp3.get(ReflectionUtil.getRangeOPLabelFromLine(line)), new Boolean(ReflectionUtil.getTransitiveFromLine(line))))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));
            final Map<String, Object> temp5 = initopobjectmap;
//            logger.info("Init Pass Of Object Properties: ");
//            initopobjectmap.values().forEach(System.out::println);                        

            Stream<String> stream5 = Files.lines(Paths.get(ontologypath));
            intermediateopobjectmap = stream5
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("OP") || (line.split(",")[0]).trim().equalsIgnoreCase("ObjectProperty")) && !(line.split(",")[5]).trim().equalsIgnoreCase("null"))
                    .map(line -> ReflectionUtil.setParentToObjectPropertyObject(temp5.get(ReflectionUtil.getOPLabelFromLine(line)), temp5.get(ReflectionUtil.getParentalOPLabelFromLine(line))))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));
            final Map<String, Object> temp12 = intermediateopobjectmap;
//            logger.info("Intermediate Object Properties: ");
//            intermediateopobjectmap.values().forEach(System.out::println);                

            //Create Final list
            finalopobjectmap = temp5.values().stream()
                    .filter(clazz -> !temp12.containsKey(ReflectionUtil.getNameOfObject(clazz)))
                    .collect(Collectors.toMap(o -> ReflectionUtil.getNameOfObject(o), o -> o));
            //append non orphan
            for (Object object : intermediateopobjectmap.values()) {
                finalopobjectmap.put(ReflectionUtil.getNameOfObject(object), object);
            }
            final Map<String, Object> temp6 = finalopobjectmap;              //FINAL OBJECT PROPERTIES
//            logger.info("\nObject Properties: ");
//            finalopobjectmap.values().forEach(System.out::println);


            /*
            *   LOAD KNOWLEDGE TRIPLES
             */
            Stream<String> stream6 = Files.lines(Paths.get(ontologypath));
            finalktriplesobjectlist = stream6
                    .filter(line -> !line.startsWith("#") && !line.trim().equalsIgnoreCase("") && ((line.split(",")[0]).trim().equalsIgnoreCase("KT") || (line.split(",")[0]).trim().equalsIgnoreCase("KnowledgeTriple"))) //&& (line.split(",")[2]).trim().equalsIgnoreCase("null")
                    .map(line -> ReflectionUtil.createIKnowledgeTriple(
                            temp4.get(  ReflectionUtil.getKTDomainFromLine(line) ),
                            temp6.get(  ReflectionUtil.getKTObjectPropertyFromLine(line) ),
                            temp4.get(  ReflectionUtil.getKTRangeFromLine(line) )
                    ))
                    .collect(Collectors.toList());

            logger.info("\nKnowledge Triples: ");
            finalktriplesobjectlist.forEach(System.out::println);            
            
            synchronized (ksession) {
                finalclassobjectmap.values().forEach(ksession::insert);
                finalinstanceobjectmap.values().forEach(ksession::insert);
                finalopobjectmap.values().forEach(ksession::insert);
                finalktriplesobjectlist.forEach(ksession::insert);
                
                //fire the rule to check logical consistency of triples
                logger.info("Fire Once in order to get the logical errors");
                
                
                ksession.fireAllRules(new AgendaFilter() {
                    public boolean accept(Match match) {
                        String rulename = match.getRule().getName();
                        if (rulename.startsWith("inference") || rulename.startsWith("debug") || rulename.startsWith("risk") ) {
                            return true;
                        }
                        return false;
                    }
                });
                //handle logical errors
                List<LogicalError> errors = new ArrayList<>();

                for (FactHandle handle : ksession.getFactHandles(new ObjectFilter() {
                    public boolean accept(Object object) {
                        if (LogicalError.class.equals(object.getClass())) {
                            return true;
                        }
                        return false;
                    }
                })) {
                    errors.add((LogicalError) ksession.getFactHandle(handle));
                }

                logger.info("Amount of Logical Errors: " + errors.size());
            }//synchronized

        } catch (IOException e) {
            logger.severe("Structural Errors During Ontology Parsing");
//            e.printStackTrace();
        }

    }//EoM

    
    public void loadPolicyModel() {    
        
    }
    
    private synchronized void setSession(KieSession session) {
        this.ksession = session;
    }

    private synchronized KieSession getSession() {
        return this.ksession;
    }

    private void addRules() {
        logger.info("Converting Rules to production memory");
        String knowledgebasename = KNOWLEDGEBASE_PREFIX + "default";   //runningapplication.getId()
        String drlPath4deployment = RULESPACKAGE + "/" + knowledgebasename + "/" + knowledgebasename + ".drl";
        try {
            String current_dir = System.getProperty("user.dir");
            Path policyPackagePath = Paths.get(current_dir + "/" + RULESPACKAGE + "/" + knowledgebasename);
            String data = "";
            //1st add default rules
            data += getDefaultRulesFromFile();

//            String data = ""
//                    + "package eu.paasword.drools;\n"
//                    + "\n"
//                    + "import eu.paasword.drools.Util;\n"
//                    + "\n"
//                    + "rule \"Class Transitiveness Inference\"\n"
//                    + "when\n"
//                    + "    $clazz: Clazz( parent != null , parent.parent != null  ) \n"
//                    + "    not ( exists(  Clazz( name==$clazz.name , parent == $clazz.parent.parent ) ) )\n"
//                    + "then\n"
//                    + "    System.out.println( \"Class Transitiveness --> New Class has to be created with name: \"+ $clazz.getName() +\" and parent: \"+$clazz.getParent().getParent().getName() );\n"
//                    + "    Clazz newclazz = new Clazz($clazz.getName(),$clazz.getParent().getParent());\n"
//                    + "    insert(newclazz);\n"
//                    + "end\n"
//                    + "\n"
//                    + "\n"
//                    + "rule \"Supertype Inheritance Inference\"\n"
//                    + "when\n"
//                    + "\n"
//                    + "    $instance: InstanceOfClazz( clazz.parent !=null   ) \n"
//                    + "    not ( exists(  InstanceOfClazz( name==$instance.name , clazz == clazz.parent ) ) )\n"
//                    + "then\n"
//                    + "    System.out.println( \"Supertype Inheritance --> New InstanceOfClass has to be created with name: \"+ $instance.getName() +\" and class: \"+$instance.getClazz().getParent().getName() );\n"
//                    + "    InstanceOfClazz newinstanceofclazz = new InstanceOfClazz($instance.getName(),$instance.getClazz().getParent());\n"
//                    + "    insert(newinstanceofclazz);\n"
//                    + "end";
            Files.createDirectories(policyPackagePath);
            FileOutputStream out = new FileOutputStream(current_dir + "/" + drlPath4deployment);
            out.write(data.getBytes());
            out.close();
            logger.info("Writing rules at: " + current_dir + "/" + RULESPACKAGE + "/" + knowledgebasename + "/" + knowledgebasename + ".drl");
            kieFileSystem.write(ResourceFactory.newFileResource(current_dir + "/" + RULESPACKAGE + "/" + knowledgebasename + "/" + knowledgebasename + ".drl"));
        } catch (Exception ex) {
            logger.severe("Error during the creation of production memory: " + ex.getMessage());
            ex.printStackTrace();
        }
    }//EoM    

    private String getDefaultRulesFromFile() {
        String ret = "";
        try {
            ret = new String(Files.readAllBytes(Paths.get(rulespath)));
        } catch (IOException ex) {
            Logger.getLogger(RulesEngineService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public void handleRequest() {

        KieSession kieSession = (KieSession) getSession();

        Clazz area = new Clazz("Area", null);
        Clazz continent = new Clazz("Continent", area);     //sub-classing will trigger "Class Transitiveness Inference"
        Clazz country = new Clazz("Country", area);
        InstanceOfClazz africa = new InstanceOfClazz("Africa", continent);

        kieSession.insert(area);
        kieSession.insert(continent);
        kieSession.insert(country);
        kieSession.insert(africa);
        kieSession.fireAllRules();

//        List facts = new ArrayList<>();
//        for (FactHandle facthandle : kieSession.getFactHandles()) {
//            logger.info( facthandle.toString() );
//        }//for        
    }//EoM    

}//EoC
