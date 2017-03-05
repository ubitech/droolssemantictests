package eu.paasword.drools.util;

import eu.paasword.drools.Clazz;
import eu.paasword.drools.ObjectProperty;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class ReflectionUtil {

    private static final Logger logger = Logger.getLogger(ReflectionUtil.class.getName());

    public static String getClassLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[1]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM    

    public static String getParentalClassLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[2]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM    

    public static String getInstanceLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[1]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM        

    public static String getInstanceClassLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[2]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM            
    
    public static String getOPLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[1]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM                
    
    public static String getDomainOPLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[2]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM                    
    
    public static String getRangeOPLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[3]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM                        

    public static String getTransitiveFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[4]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM   
    
    public static String getParentalOPLabelFromLine(String line) {
        String ret = "";
        ret = (line.split(",")[5]).trim().replaceAll(" ", "_");
        return ret;
    }//EoM      
    
    public static Object createOrphanClazz(String label) {
        Object retobject = null;
        try {
            Class orhpanclass = Class.forName("eu.paasword.drools.Clazz");
            retobject = orhpanclass.getDeclaredConstructor(String.class).newInstance(label);

        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return retobject;
    }//EoM

    public static Object createNonOrphanClazz(String label, Object parent) {
        Object retobject = null;
        try {
            Class orhpanclass = Class.forName("eu.paasword.drools.Clazz");
            retobject = orhpanclass.getDeclaredConstructor(String.class, Clazz.class).newInstance(label, parent);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return retobject;
    }//EoM    

    public static Object setParentToClazzObject(Object child, Object parent) {
        Object retobject = null;
        try {
            Class clazz = child.getClass();
            Method method = clazz.getMethod("setParent", new Class[]{Clazz.class});
            //logger.info(getNameOfClazzObject(child) + " setParent " + getNameOfClazzObject(parent) +" method "+method.getName() +" isNull?"+ (method==null) );
            method.invoke(child, new Object[]{parent});
            retobject = child;
            //logger.info("Result " + retobject.toString());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.severe(ex.getMessage());
        }
        return retobject;
    }//EoM        

    public static Object createInstanceOfClazz(String label, Object clazz) {
        Object retobject = null;
        try {
            Class instanceofclass = Class.forName("eu.paasword.drools.InstanceOfClazz");
            retobject = instanceofclass.getDeclaredConstructor(String.class, Clazz.class).newInstance(label, clazz);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return retobject;
    }//EoM    

    public static Object createOrphanObjectProperty(String label, Object domain, Object range, boolean transitive) {
        Object retobject = null;
        try {
            Class instanceofclass = Class.forName("eu.paasword.drools.ObjectProperty");
            retobject = instanceofclass.getDeclaredConstructor(String.class, Clazz.class , Clazz.class, boolean.class , ObjectProperty.class).newInstance(label, domain, range, transitive, null);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return retobject;
    }//EoM       
    
    public static Object setParentToObjectPropertyObject(Object child, Object parent) {
        Object retobject = null;
        try {
            Class clazz = child.getClass();
            Method method = clazz.getMethod("setParent", new Class[]{ObjectProperty.class});
            //logger.info(getNameOfClazzObject(child) + " setParent " + getNameOfClazzObject(parent) +" method "+method.getName() +" isNull?"+ (method==null) );
            method.invoke(child, new Object[]{parent});
            retobject = child;
//            logger.info("Result " + retobject.toString());
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            logger.severe(ex.getMessage());
        }
        return retobject;
    }//EoM     
    
    public static String getNameOfObject(Object object) {
        String retname = null;
        try {
            retname = (String) invokeMethod(object, "getName");
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(ReflectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retname;
    }//EoM

    public static Object invokeMethod(Object object, String methodname) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class clazz = object.getClass();
        Method method = clazz.getMethod(methodname, new Class[]{});
        return method.invoke(object, new Object[]{});
    }//EoM    

}
