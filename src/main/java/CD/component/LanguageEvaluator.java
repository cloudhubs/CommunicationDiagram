package CD.component;

import CD.enums.Language;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static CD.component.Utility.createResponseEntity;
import static CD.constants.PY_PARSER_LANGAUGE_URL;

@Component
public class LanguageEvaluator {

    public Language getModuleLanguage(String moduleName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", moduleName);
        return Language.getFromString(createResponseEntity(PY_PARSER_LANGAUGE_URL, map, String.class));
    }
}
