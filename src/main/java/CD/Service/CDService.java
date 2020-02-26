/******************************************************************************
 *
 * CDService.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.Service;

import CD.component.JParserCaller;
import CD.component.LanguageEvaluator;
import CD.component.PyParserCaller;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.CommunicationRequest;
import CD.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * CDService
 *
 * Service for getting communication diagrams
 */
@Service
public class CDService {

    // generating java cd
    @Autowired
    private JParserCaller jParserCaller;

    // determines what the language of a module is
    @Autowired
    private LanguageEvaluator languageEvaluator;

    // for generating python communication diagrams
    @Autowired
    private PyParserCaller pyParserCaller;

    /**
     * creates a communication diagram for a module
     * @param req the request
     *
     * @return the communication diagram
     */
    public Communication createCommunicationDiagram(CommunicationRequest req){

        // get the location of the module
        String moduleLocation = req.getMicroservice();

        // figure out what language it is written in
        Language language = languageEvaluator.getModuleLanguage(moduleLocation);

        // call the appropriate microservice to evaluate
        return language == Language.PYTHON ? pyParserCaller.getSystem(moduleLocation) : jParserCaller.getSystem(moduleLocation);

    }
}
