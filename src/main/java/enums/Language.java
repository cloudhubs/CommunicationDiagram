package enums;

public enum Language {
    JAVA, PYTHON, OTHER;

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
