package CD.Service;

import CD.component.JParserCaller;
import CD.component.LanguageEvaluator;
import CD.component.PyParserCaller;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.CommunicationRequest;
import CD.enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CDService {

    @Autowired
    private JParserCaller jParserCaller;

    @Autowired
    private LanguageEvaluator languageEvaluator;

    @Autowired
    private PyParserCaller pyParserCaller;

    public Communication createCommunicationDiagram(CommunicationRequest req){

        String moduleLocation = req.getMicroservice();

        //first figure out what language it is written in
        Language language = languageEvaluator.getModuleLanguage(moduleLocation);

        return language == Language.PYTHON ? pyParserCaller.getSystem(moduleLocation) : jParserCaller.getSystem(moduleLocation);

    }
}
