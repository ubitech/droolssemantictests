package eu.paasword.drools.config;

import eu.paasword.drools.service.RulesEngineService;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
@Configuration
public class Initializer {

    private static final Logger logger = Logger.getLogger(Initializer.class.getName());
    public static final String PACKAGE_NAME = "eu.paasword.drools";
    public static final String KIE_BASE_MODEL_PREFIX = "PaaswordKnowledgeBase_";
    public static final String KNOWLEDGEBASE_PREFIX = "kb";
    public static final String FACT_KNOWLEDGEBASE_BASE_PREFIX = "PaaswordKnowledgeBase_kb";    
    public static final String RULESPACKAGE = "rules";
    public static final String SESSION_PREFIX = "RulesEngineSession_";
    public static final String FACT_SESSION_PREFIX = "RulesEngineSession_kb";    

    @Order(1)
    @Bean
    public KieContainer loadProductionMemory() {             
        logger.info("Ready to create a new blank Container");
        ReleaseId releaseId = KieServices.Factory.get().newReleaseId(PACKAGE_NAME, "expert-system", "1.0");
        KieServices kieServices = KieServices.Factory.get();
        KieFileSystem kieFileSystem = kieServices.newKieFileSystem();
        KieModuleModel kieModuleModel = kieServices.newKieModuleModel();
        String knowledgebasename = KNOWLEDGEBASE_PREFIX + "0";  //default running id
        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel(KIE_BASE_MODEL_PREFIX + knowledgebasename).setDefault(true); //.setEventProcessingMode(EventProcessingOption.STREAM);
        kieBaseModel1.addPackage(RULESPACKAGE + "." + knowledgebasename);
        String factSessionName = SESSION_PREFIX + knowledgebasename;
        kieBaseModel1.newKieSessionModel(factSessionName);                                  //.setClockType(ClockTypeOption.get("realtime"));
        KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
        kieFileSystem.generateAndWritePomXML(releaseId);
        kieFileSystem.writeKModuleXML(kieModuleModel.toXML());
        kieBuilder.buildAll();
        logger.info("Blank container created ");
        return KieServices.Factory.get().newKieContainer(releaseId);
    }//EoM    

    /**
     * Load the production memory.
     *
     * @param rulesEngineService
     * @return
     */
    @Order(2)
    @Bean
    public KieContainer populateProductionMemory(RulesEngineService rulesEngineService) {
        try {
            String current_dir = System.getProperty("user.dir");
            logger.info("Using current directory: " + current_dir);
            //TODO create /rules if it does not exist
            new File(current_dir + "/" + RULESPACKAGE).mkdirs();
            FileUtils.cleanDirectory(new File(current_dir + "/" + RULESPACKAGE));
        } catch (IOException ex) {
            logger.severe("Error cleaning the rules directory" + ex.getMessage());
        }
        //create knowledge base
        rulesEngineService.createKnowledgebase();

        logger.info("Ready to launch KIE Container");
        return rulesEngineService.launchKieContainer();

    }//EoM    

}//EoC
