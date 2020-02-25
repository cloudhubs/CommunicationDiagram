/******************************************************************************
 *
 * PyParserCaller.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.component;

import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.pyparser.PySystem;
import org.springframework.stereotype.Component;

import static CD.util.Utility.createResponseEntity;
import static CD.constants.PY_PARSER_PARSE_URL;

import java.util.HashMap;
import java.util.Map;

@Component
public class PyParserCaller implements ParserCaller{

    public Communication getSystem(String systemName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", systemName);
        PySystem system = createResponseEntity(PY_PARSER_PARSE_URL, map, PySystem.class);
        return null; //TODO
    }

}
