/******************************************************************************
 *
 * LanguageEvaluator.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.component;

import CD.enums.Language;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static CD.util.Utility.createResponseEntity;
import static CD.constants.PY_PARSER_LANGAUGE_URL;

/**
 * finds out what the language of the system is
 *
 * @author Ian laird
 */
@Component
public class LanguageEvaluator {

    /**
     * finds the language of the module
     * @param moduleName the module to analyze
     * @return the language of the module
     */
    public Language getModuleLanguage(String moduleName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", moduleName);
        return Language.getFromString(createResponseEntity(PY_PARSER_LANGAUGE_URL, map, String.class));
    }
}
