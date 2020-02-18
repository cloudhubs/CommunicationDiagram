package component;

import edu.baylor.ecs.cloudhubs.prophetdto.pyparser.PySystem;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class PyParserCaller {

    public final static String PY_PARSER_BASE_URL = "localhost:8081";
    public final static String PY_PARSER_PARSE_URL = PY_PARSER_BASE_URL + "/parser";

    public PySystem getSystem(String systemName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", systemName);
        return createResponseEntity(PY_PARSER_PARSE_URL, map, PySystem.class);
    }

    public static<T> T createResponseEntity(String url, Map<String, Object> contentMap, Class classType){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(contentMap, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<T> response = restTemplate.getForEntity(PY_PARSER_PARSE_URL, classType, entity);
        return response.getBody();
    }

}
