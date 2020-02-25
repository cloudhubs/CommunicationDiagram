/******************************************************************************
 *
 * ParserCaller.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.component;

import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;

public interface ParserCaller {

    public Communication getSystem(String systemName);
}
