package CD.build;

import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;

import java.util.Stack;

public class CDBuilder {

    private Stack<FunctionContext> callStack = new Stack<>();
    private StringStack sequenceNumber = new StringStack();
    private boolean ifconditionFlag = false;
    private boolean startFunctionFlag = false;
    private String functionArguments = null;

    public void consume(AbstractToken token){
        FunctionContext currentContext = callStack.peek();

        switch(token.getType()){

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
                if(startFunctionFlag){
                    startFunctionFlag = false;
                    callStack.push(new FunctionContext());
                    // create an edge for this call !!!! TODO
                }
                break;
            case END_FUNCTION:
                callStack.pop();
                callStack.peek().setSeqNum(callStack.peek().getSeqNum() + 1);
                break;

            // if not one of the expected token
            case OTHER:
                if(ifconditionFlag){
                    currentContext.getIfStack().push(token.toString());
                    ifconditionFlag = false;
                }
                else if(startFunctionFlag){
                    functionArguments = token.toString();
                    functionArguments = null;
                }
                break;
        }
    }

    public Communication build() throws BuildException {
        return null;
    }
}
