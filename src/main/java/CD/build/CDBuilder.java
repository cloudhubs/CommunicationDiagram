package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.*;

public class CDBuilder {

    // used to interpret tokens once the program has begun executing
    private ProgramInterpretor programInterpretor = new ProgramInterpretor();

    // used to interpret the header of the program
    private HeaderInterpretor headerInterpretor = new HeaderInterpretor();

    // meant to mimic the runtime stack
    private Stack<FunctionContext> callStack = new Stack<>();

    // used to create sequence numbers of communications
    private StringStack sequenceNumber = new StringStack();

    // the types of objects that are defined for this program
    private Set<String> types = new HashSet<>();

    // the methods defined for all objects in this program
    private Set<Method> methods = new HashSet<>();

    // the nodes for the communication diagram
    private Set<Node> nodes = new HashSet<>();

    // the edges for the communication diagram
    private Set<Edge> edges = new HashSet<>();

    // indicates that the program has begun executing
    private boolean program_begun = false;

    // indicates that the program has finished executing
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
            program_begun = headerInterpretor.consume(token);
            if(program_begun){
                this.types = headerInterpretor.getTypes();
                this.methods = headerInterpretor.getMethods();
            }
        }
        else{
            headerInterpretor.consume(token);
        }
    }


    public Communication build() throws BuildException {
        return null;
    }
}
