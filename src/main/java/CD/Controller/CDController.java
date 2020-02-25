/******************************************************************************
 *
 * CDController.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.Controller;

import CD.Service.CDService;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.CommunicationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CDController {

     @Autowired
     private CDService communicationService;

     @GetMapping("generateDiagram")
     public Communication createCommunicationDiagram(@RequestBody CommunicationRequest req){
          return communicationService.createCommunicationDiagram(req);
     }
}
