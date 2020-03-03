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

import CD.build.converter.JParserConverter;
import edu.baylor.ecs.cloudhubs.jparser.component.context.AnalysisContext;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import org.springframework.stereotype.Component;
import static CD.util.Utility.createResponseEntity;
import static CD.constants.JPARSER_PARSER_URL;

import java.util.HashMap;
import java.util.Map;

/**
 * Communicates with JParser
 */
@Component
public class JParserCaller implements ParserCaller{

    /**
     * calls the JParser endpoint to get the parsed Java source code
     * @param systemName the name of the system that JParser is to parser
     * @return Communication Diagram for this sytem
     */
    public Communication getSystem(String systemName){
        Map<String, Object> map = new HashMap<>();
        map.put("filepath", systemName);
        AnalysisContext analysisContext = createResponseEntity(JPARSER_PARSER_URL, map, AnalysisContext.class);
        return JParserConverter.convert(analysisContext);
    }
}
