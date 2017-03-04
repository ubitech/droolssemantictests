package eu.paasword.drools.service;

import eu.paasword.drools.Clazz;
import eu.paasword.drools.InstanceOfClazz;
import static eu.paasword.drools.config.Initializer.FACT_KNOWLEDGEBASE_BASE_PREFIX;
import static eu.paasword.drools.config.Initializer.FACT_SESSION_PREFIX;
import static eu.paasword.drools.config.Initializer.KIE_BASE_MODEL_PREFIX;
import static eu.paasword.drools.config.Initializer.KNOWLEDGEBASE_PREFIX;
import static eu.paasword.drools.config.Initializer.PACKAGE_NAME;
import static eu.paasword.drools.config.Initializer.RULESPACKAGE;
import static eu.paasword.drools.config.Initializer.SESSION_PREFIX;
import eu.paasword.drools.util.KieUtil;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
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
import org.kie.api.runtime.conf.BeliefSystemTypeOption;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.io.ResourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    KieContainer kieContainer;

    @Autowired
    public RulesEngineService(KieUtil kieUtil) {
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
        addPolicyRules();
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

    public void loadOntology(){
        
    }
    
    private synchronized void setSession(KieSession session) {
        this.ksession = session;
    }

    private synchronized KieSession getSession() {
        return this.ksession;
    }

    public void addPolicyRules() {
        logger.info("Converting Rules to production memory");
        String knowledgebasename = KNOWLEDGEBASE_PREFIX + "default";   //runningapplication.getId()
        String drlPath4deployment = RULESPACKAGE + "/" + knowledgebasename + "/" + knowledgebasename + ".drl";
        try {
            String current_dir = System.getProperty("user.dir");
            Path policyPackagePath = Paths.get(current_dir + "/" + RULESPACKAGE + "/" + knowledgebasename);
            String data = ""
                    + "package eu.paasword.drools;\n"
                    + "\n"
                    + "import eu.paasword.drools.Util;\n"
                    + "\n"
                    + "rule \"Class Transitiveness Inference\"\n"
                    + "when\n"
                    + "    $clazz: Clazz( parent != null , parent.parent != null  ) \n"
                    + "    not ( exists(  Clazz( name==$clazz.name , parent == $clazz.parent.parent ) ) )\n"
                    + "then\n"
                    + "    System.out.println( \"Class Transitiveness --> New Class has to be created with name: \"+ $clazz.getName() +\" and parent: \"+$clazz.getParent().getParent().getName() );\n"
                    + "    Clazz newclazz = new Clazz($clazz.getName(),$clazz.getParent().getParent());\n"
                    + "    insert(newclazz);\n"
                    + "end\n"
                    + "\n"
                    + "\n"
                    + "rule \"Supertype Inheritance Inference\"\n"
                    + "when\n"
                    + "\n"
                    + "    $instance: InstanceOfClazz( clazz.parent !=null   ) \n"
                    + "    not ( exists(  InstanceOfClazz( name==$instance.name , clazz == clazz.parent ) ) )\n"
                    + "then\n"
                    + "    System.out.println( \"Supertype Inheritance --> New InstanceOfClass has to be created with name: \"+ $instance.getName() +\" and class: \"+$instance.getClazz().getParent().getName() );\n"
                    + "    InstanceOfClazz newinstanceofclazz = new InstanceOfClazz($instance.getName(),$instance.getClazz().getParent());\n"
                    + "    insert(newinstanceofclazz);\n"
                    + "end";
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
