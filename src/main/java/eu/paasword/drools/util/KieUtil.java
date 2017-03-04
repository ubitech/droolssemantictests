
package eu.paasword.drools.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

/**
 *
 * @author Eleni Fotopoulou <efotopoulou@ubitech.eu>
 */
@Component
public class KieUtil {

    private static final Logger logger = Logger.getLogger(KieUtil.class.getName());    
    
    private final ConcurrentHashMap threadmap = new ConcurrentHashMap();

    private void updateMap() {
        ThreadsGroup tg = ThreadsGroupFactory.getThreadGroup();
        tg.updateThreadMap(threadmap);
    }

    private void addToThreadMap(String factSessionName, KieSession kiesession) {
        ThreadsGroup tg = ThreadsGroupFactory.getThreadGroup();
        tg.addToThreadMap(factSessionName, kiesession);
    }

    private void removeFromThreadMap(String factSessionName) {
        ThreadsGroup tg = ThreadsGroupFactory.getThreadGroup();
        tg.removeFromThreadMap(factSessionName);
    }

    public ConcurrentHashMap seeThreadMap() {
        ThreadsGroup tg = ThreadsGroupFactory.getThreadGroup();
        return tg.getThreadMap();
    }

    public void fireKieSession(KieSession kieSession, String factSessionName) {
        Thread t = new Thread() {
            @Override
            public void run() {
                kieSession.fireUntilHalt();
            }
        };
        t.setName(factSessionName);
        logger.info("New session Thread creation" + t.toString());
        threadmap.put(factSessionName, kieSession);
        t.start();
        addToThreadMap(factSessionName, kieSession);
    }//EoM

    public void haltKieSession(String factSessionName) {
        KieSession kieSession =  (KieSession) threadmap.get(factSessionName);
        if (kieSession != null) {
            kieSession.halt();
            kieSession.dispose();
            //t.stop();
            threadmap.remove(kieSession);
            //updateMap();
            removeFromThreadMap(factSessionName);
        }//if

    }//EoM   

}
