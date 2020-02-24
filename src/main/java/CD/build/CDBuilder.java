package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.*;

public class CDBuilder {

    private ProgramInterpretor programInterpretor = new ProgramInterpretor();
    private HeaderInterpretor headerInterpretor = new HeaderInterpretor();

    private Stack<FunctionContext> callStack = new Stack<>();
    private StringStack sequenceNumber = new StringStack();
    private Set<String> types = new HashSet<>();
    private Set<Method> methods = new HashSet<>();
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();
    private boolean program_begun = false;
    private boolean program_terminated = false;

    /**
     * consumes a token
     *
     * @param token
     */
    public void consume(AbstractToken token) throws BuildException{
        if(program_terminated){
            throw new BuildException("END_PROGRAM already encountered");
        }

        if(!program_begun){
            program_begun = headerInterpretor.consumeBeforeStart(token);
            if(program_begun){
                this.types = headerInterpretor.getTypes();
                this.methods = headerInterpretor.getMethods();
            }
        }
        else{
            headerInterpretor.consumeBeforeStart(token);
        }
    }


    public Communication build() throws BuildException {
        return null;
    }
}
