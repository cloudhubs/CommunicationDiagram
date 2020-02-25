/******************************************************************************
 *
 * Constants.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD;

public class constants {

    public static final boolean IS_JAVA = true;
    public static final boolean IS_PYTHON = false;

    public final static String PY_PARSER_BASE_URL = "localhost:8081";
    public final static String PY_PARSER_PARSE_URL = PY_PARSER_BASE_URL + "/parser";
    public static final String PY_PARSER_LANGAUGE_URL = PY_PARSER_BASE_URL + "/langauge";
    public static final String JPARSER_BASE_URL = "localhost:9090";
    public static final String JPARSER_PARSER_URL = "/analysis";
}
