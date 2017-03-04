package eu.paasword.drools;

/**
 *
 * @author Panagiotis Gouvas (pgouvas@ubitech.eu)
 */
public class Util {

    public static boolean isClassSubclassOfClass(Clazz test, Clazz parent) {
        if (test.equals(parent)) {
            return true;
        }
        if (!test.equals(parent) && test.getParent() == null) {
            return false;
        } else return isClassSubclassOfClass(test.getParent(),parent);
    }//EoM

}//EoM
