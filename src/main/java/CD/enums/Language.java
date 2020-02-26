/******************************************************************************
 *
 * Language.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.enums;

/**
 * Currently the only supported languages are Java and Python
 */
public enum Language {

    JAVA,
    PYTHON,
    OTHER;

    /**
     * converts String to corresponding Enum
     * @param s the string
     * @return the enum
     */
    public static Language getFromString(String s){
        switch(s){
            case "JAVA":
                return JAVA;
            case "PYTHON":
                return PYTHON;
            default:
                return OTHER;
        }
    }
}
