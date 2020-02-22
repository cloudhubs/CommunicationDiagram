package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.*;

public class CDBuilder {

    private Stack<FunctionContext> callStack = new Stack<>();
    private StringStack sequenceNumber = new StringStack();
    private Set<String> types = new HashSet<>();
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();
    private List<AbstractToken> arguments = new LinkedList<>();
    private Instruction lastEncounteredInstruction = null;
    private int tokenCount = 0;

    private boolean ifconditionFlag = false;
    private int startFunctionFlag = 0;
    private boolean createVariableFlag = false;
    private String functionArguments = null;
    private String functionObjectBeingCalled = null;

    /**
     * consumes a token
     *
     * instruction types:
     *
     * DEFINE_TYPES: all unrecognized instructions after this are considered class names
     *
     * DEFINE_FUNC: creating a function definition
     *
     * END_DEFINE_FUNC: ends a function definition
     *
     * START_IF: begins conditional execution of the following code
     *      params: condition
     *
     * END_IF: ends last START_IF
     *
     * CALL_FUNCTION: indicates that a function is going to be called
     *  params: objectName functionName
     *  NOTE: params token must be followed by another START_FUNCTION
     *
     * @param token
     */
    public void consume(AbstractToken token) throws BuildException{
        if(tokenCount == 0){
            if(token.getType() != Instruction.DEFINE_TYPES){
                throw new BuildException("First instruction must be DEFINE_TYPES");
            }
        }
        if(token.getType() == Instruction.OTHER){
            arguments.add(token);
            return;
        }
        FunctionContext currentContext = callStack.peek();

        // if not an argument it is another command to execute
        // therefore execute the last command
        switch(lastEncounteredInstruction){
            case DEFINE_TYPES:
                arguments.forEach(x -> types.add(x.toString()));
                break;
            case DEFINE_FUNC:
                String ownerClass;
                String methodName;
                List<AbstractToken> instructions;
                break;
            case END_DEFINE_FUNC:
                break;
            case DECLARE:
                break;
            case CALL_FUNCTION:
                break;
            case BEGIN_IF:
                break;
            case END_IF:
                break;
            case BEGIN_HEADER:
                break;
            case END_HEADER:
                break;
            case OTHER:
                break;

        }

        /*
        switch(token.getType()){

            // indicates that a variable is being declared
            case CREATE:
                createVariableFlag = true;
                break;

            // if an if statement or an elif, the following stmt will be included as a condition
            case START_IF:
                ifconditionFlag = true;
                break;

            // if an if statment is ending, pop the condition of the if statement from the condition
            case END_IF:
                ifconditionFlag = false;
                String discard = currentContext.getIfStack().pop();
                currentContext.getDiscardedIfStack().push(discard);
                break;

            // for an else just negate the previous if
            case START_ELSE:
                String elseStmt = currentContext.getDiscardedIfStack().pop();
                elseStmt = "!(" + elseStmt + ")";
                currentContext.getIfStack().push(elseStmt);
                break;

            // because an else is the end of an if block no more elses will follow with that condition
            case END_ELSE:
                currentContext.getIfStack().pop();
                break;

            // if a function is starting the next token will be the called function and the following will be params or START_FUNCTION
            case START_FUNCTION:

                // if the function has already been initiated this begins the code for the next function
                if(startFunctionFlag > 0){
                    startFunctionFlag = 0;
                    callStack.push(new FunctionContext());
                    // create an edge for this call !!!! TODO
                }
                else{
                    startFunctionFlag = 1;
                }
                break;
            case END_FUNCTION:
                callStack.pop();
                callStack.peek().setSeqNum(callStack.peek().getSeqNum() + 1);
                break;

            // if not one of the expected token
            case OTHER:
                if(createVariableFlag){
                    nodes.add(new Node(token.toString()));
                }
                else if(ifconditionFlag){
                    currentContext.getIfStack().push(token.toString());
                    ifconditionFlag = false;
                }
                else if(startFunctionFlag > 0){
                    switch(startFunctionFlag){
                        case 1:
                            functionObjectBeingCalled = token.toString();
                            break;
                        case 2:
                            functionArguments = token.toString();
                    }
                    ++ startFunctionFlag;
                }
                break;
        }
        */
        return;
    }
    public Communication build() throws BuildException {
        return null;
    }
}
