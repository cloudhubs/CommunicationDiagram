package component;

import enums.Language;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static component.PyParserCaller.PY_PARSER_BASE_URL;
import static component.PyParserCaller.createResponseEntity;

@Component
public class LanguageEvaluator {

    public static final String PY_PARSER_LANGAUGE_URL = PY_PARSER_BASE_URL + "/langauge";

    public Language getModuleLanguage(String moduleName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", moduleName);
        return Language.getFromString(createResponseEntity(PY_PARSER_BASE_URL, map, String.class));
    }
}
