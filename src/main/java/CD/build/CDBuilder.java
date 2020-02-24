package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.*;

import static CD.build.Instruction.*;

public class CDBuilder {

    private Stack<FunctionContext> callStack = new Stack<>();
    private StringStack sequenceNumber = new StringStack();
    private Set<String> types = new HashSet<>();
    private Set<Node> nodes = new HashSet<>();
    private Set<Edge> edges = new HashSet<>();
    private List<AbstractToken> arguments = new LinkedList<>();
    private Instruction lastEncounteredInstruction = null;
    private int tokenCount = 0;
    private boolean program_begun = false;
    private boolean program_terminated = false;

    private boolean ifconditionFlag = false;
    private int startFunctionFlag = 0;
    private boolean createVariableFlag = false;
    private String objectName = null;
    private String methodName = null;

    /**
     * consumes a token
     *
     * @param token
     */
    public void consume(AbstractToken token) throws BuildException{
        if(program_terminated){
            throw new BuildException("END_PROGRAM already encountered");
        }

        if(program_begun){
            consumeAfterStart(token);
        }
        else{
            consumeBeforeStart(token);
        }
        return;
    }

    /**
     * consumes a token
     *
     * This is used for consuming a token before the program has started. It is useful for declaring types and declaring functions.
     *
     * This section can be thought of as a compiler because it analyzes chunks of code at a time.
     *
     *   DEFINE_TYPES: all unrecognized instructions after this are considered class names
     *
     *   DEFINE_FUNC: creating a function definition
     *
     *   END_DEFINE_FUNC: ends a function definition
     *
     * @param token the token to consume
     * @throws BuildException if invalid token
     */
    public void consumeBeforeStart(AbstractToken token) throws BuildException{
        if(tokenCount == 0){
            if(token.getType() != Instruction.DEFINE_TYPES){
                throw new BuildException("First instruction must be DEFINE_TYPES");
            }
        }
        if((token.getType() == Instruction.OTHER) || ((lastEncounteredInstruction == DEFINE_FUNC) && (token.getType() != END_DEFINE_FUNC))){
            arguments.add(token);
            return;
        }
        FunctionContext currentContext = callStack.peek();

        // if not an argument it is another command to execute
        // therefore execute the last command
        switch(lastEncounteredInstruction){

            // for DEFINE_TYPES each argument is a type
            case DEFINE_TYPES:
                arguments.forEach(x -> types.add(x.toString()));
                break;

            // definition of a function
            case DEFINE_FUNC:
                String ownerClass;
                String methodName;
                List<AbstractToken> instructions;
                break;

            case END_DEFINE_FUNC:
                break;

            case BEGIN_PROGRAM:
                this.program_begun = true;
                break;

        }
        lastEncounteredInstruction = token.getType();
    }

    /**
     * consumes a token
     *
     * This is used for consuming a token as a program is running. This functions as an interpreter.
     *
     *  START_IF: begins conditional execution of the following code
     *       params: condition
     *
     *  END_IF: ends last START_IF
     *
     *  CALL_FUNCTION: indicates that a function is going to be called
     *   params: objectName functionName
     *
     * @param token the token to consume
     * @throws BuildException if invalid token
     */
    public void consumeAfterStart(AbstractToken token) throws BuildException{
        if(!program_begun) {
            throw new BuildException("Program not begun");
        }

        FunctionContext currentContext = callStack.peek();

        switch(token.getType()){

            // indicates that a variable is being declared
            case DECLARE:
                createVariableFlag = true;
                break;

            // if an if statement or an elif, the following stmt will be included as a condition
            case BEGIN_IF:
                ifconditionFlag = true;
                break;

            // if an if statment is ending, pop the condition of the if statement from the condition
            case END_IF:
                ifconditionFlag = false;
                String discard = currentContext.getIfStack().pop();
                currentContext.getDiscardedIfStack().push(discard);
                break;

            // for an else just negate the previous if
            case BEGIN_ELSE:
                String elseStmt = currentContext.getDiscardedIfStack().pop();
                elseStmt = "!(" + elseStmt + ")";
                currentContext.getIfStack().push(elseStmt);
                break;

            // because an else is the end of an if block no more elses will follow with that condition
            case END_ELSE:
                currentContext.getIfStack().pop();
                break;

            // if a function is starting the next token will be the called function and the following will be params or START_FUNCTION
            case CALL_FUNCTION:
                startFunctionFlag = 1;

                break;
            case END_FUNCTION_CALL:
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
                            this.objectName = token.toString();
                            break;
                        case 2:
                            this.methodName = token.toString();
                            callStack.push(new FunctionContext());
                    }
                    ++ startFunctionFlag;
                }
                break;
            case END_PROGRAM:
                this.program_terminated = true;
                break;
        }
    }

    public Communication build() throws BuildException {
        return null;
    }
}
