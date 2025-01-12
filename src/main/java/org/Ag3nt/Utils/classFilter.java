package org.Ag3nt.Utils;

public class classFilter {
    static String []interestClasses = new String[]{
            "HelloWorld"
    };

    public static boolean isInterestClass(String className){
        for (String interestClass : interestClasses) {
            if (className.contains(interestClass)) {
                return true;
            }
        }
        return false;
    }
}
