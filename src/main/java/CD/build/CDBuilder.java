package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class CDBuilder {

    private Stack<FunctionContext> callStack = new Stack<>();
    private StringStack sequenceNumber = new StringStack();
    private boolean ifconditionFlag = false;
    private int startFunctionFlag = 0;
    private boolean createVariableFlag = false;
    private String functionArguments = null;
    private String functionObjectBeingCalled = null;

    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();

    /**
     * consumes a token
     *
     * token types:
     *
     * CREATE: indicates that an object is going to be created
     *      params: objectType objectName
     *
     * START_IF: begins conditional execution of the following code
     *      params: condition
     *
     * END_IF: ends last START_IF
     *
     * START_ELSE: starts block that will execute if the last IF did not execute
     *
     * END_ELSE: ends ELSE block
     *
     * START_FUNCTION: indicates that a function is going to be called
     *  params: objectName functionName
     *  NOTE: params token must be followed by another START_FUNCTION
     *
     *  Example:
     *      START_FUNCTION bar foo START_FUNCTION
     *
     * END_FUNCTION: indicates that the function is ending
     *
     * @param token
     */
    public void consume(AbstractToken token){
        FunctionContext currentContext = callStack.peek();

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
        return;
    }

    public Communication build() throws BuildException {
        return null;
    }
}
