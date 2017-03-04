package eu.paasword.drools.main;

import java.util.logging.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
@ComponentScan({
    "eu.paasword.drools.config",    
    "eu.paasword.drools.service",
    "eu.paasword.drools.util",
    "eu.paasword.drools.rest"    
})
@EnableAutoConfiguration
public class Application extends SpringBootServletInitializer {

    private static final Logger logger = Logger.getLogger(Application.class.getName());

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
