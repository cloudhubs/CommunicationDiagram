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

/**
 * Creates a Communication Diagram for a Python module using PyCharm
 */
@Component
public class PyParserCaller implements ParserCaller{

    /**
     * calls the Py Parser endpoint to get the parsed Java source code
     * @param systemName the name of the system that Py Parser is to parse
     * @return Communication Diagram for this sytem
     */
    public Communication getSystem(String systemName){
        Map<String, Object> map = new HashMap<>();
        map.put("fileName", systemName);
        PySystem system = createResponseEntity(PY_PARSER_PARSE_URL, map, PySystem.class);
        return null; //TODO
    }

}
