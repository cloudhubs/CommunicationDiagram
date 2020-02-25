/******************************************************************************
 *
 * JParserCaller.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.component;

import edu.baylor.ecs.cloudhubs.jparser.component.context.AnalysisContext;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import org.springframework.stereotype.Component;
import static CD.util.Utility.createResponseEntity;
import static CD.constants.JPARSER_PARSER_URL;

import java.util.HashMap;
import java.util.Map;

@Component
public class JParserCaller implements ParserCaller{

    public Communication getSystem(String systemName){
        Map<String, Object> map = new HashMap<>();
        map.put("filePath", systemName);
        AnalysisContext analysisContext = createResponseEntity(JPARSER_PARSER_URL, map, AnalysisContext.class);
        return null;
    }
}
