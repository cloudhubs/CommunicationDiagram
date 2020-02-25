/******************************************************************************
 *
 * ProgramInterprator.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/


package CD.build.interpretor;

import CD.build.token.AbstractToken;
import CD.build.FunctionContext;
import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.Set;
import java.util.Stack;

/**
 * consumes tokens after a BEGIN_PROGRAM has been seen
 */
public class ProgramInterpretor {

    // Class variables /////////////////////////////////////////////

    // set if a BEGIN_IF was the last seen token
    private boolean ifconditionFlag;

    // counts how far away from the BEGIN_FUNCTION
    private int startFunctionFlag;

    // set if a CREATE has been seen
    private boolean createVariableFlag;

    // the class name of the method holder
    private String objectName;

    // the method name
    private String methodName;

    ///////////////////////////////////////////////////////////////////

    /**
     * default constructor
     */
    public ProgramInterpretor(){
        ifconditionFlag = false;
        startFunctionFlag = 0;
        createVariableFlag = false;
        objectName = null;
        methodName = null;
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
    public boolean consume(AbstractToken token, Stack<FunctionContext> callStack, StringStack sequenceNumber, Set<Node> nodes, Set<Edge> edges) throws BuildException{

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

                            // create an edge for this call
                            edges.add(new Edge(
                                    null,
                                    sequenceNumber.toString(".", "", ""), // the seq number
                                    currentContext.getFunctionName(), // the start node
                                    this.methodName                   // the end node
                            ));

                            callStack.push(new FunctionContext());
                    }
                    ++ startFunctionFlag;
                }
                break;
            case END_PROGRAM:
                return true;
        }
        return false;
    }
}
