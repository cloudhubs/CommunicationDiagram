package Service;

import component.LanguageEvaluator;
import component.PyParserCaller;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.CommunicationRequest;
import edu.baylor.ecs.cloudhubs.prophetdto.pyparser.PySystem;
import enums.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CDService {

    @Autowired
    private LanguageEvaluator languageEvaluator;

    @Autowired
    private PyParserCaller pyParserCaller;

    public Communication createCommunicationDiagram(CommunicationRequest req){

        String moduleLocation = req.getMicroservice();

        //first figure out what language it is written in
        Language language = languageEvaluator.getModuleLanguage(moduleLocation);

        switch(language){
            case JAVA:
                // blah blah
                return null;
            case PYTHON:
                PySystem pySystem = pyParserCaller.getSystem(moduleLocation);
        }

        return null;
    }
}
