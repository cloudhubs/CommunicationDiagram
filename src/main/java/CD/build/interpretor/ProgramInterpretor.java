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

import CD.build.Method;
import CD.build.token.AbstractToken;
import CD.build.FunctionContext;
import CD.build.token.Instruction;
import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;

/**
 * consumes tokens after a BEGIN_PROGRAM has been seen
 */
public class ProgramInterpretor {

    // Class variables /////////////////////////////////////////////

    private Set<String> types;

    // set if a BEGIN_IF was the last seen token
    private int ifconditionFlag;

    // counts how far away from the BEGIN_FUNCTION
    private int startFunctionFlag;

    // set if a CREATE has been seen
    private int createVariableFlag;

    // the class name of the method holder
    private String className;

    // the method name
    private String methodName;

    // the instance name
    private String instancenName;

    // indicates that END_PROGRAM has been encounterd
    public boolean programCompleted;

    ///////////////////////////////////////////////////////////////////

    /**
     * default constructor
     */
    public ProgramInterpretor(Set<String> types){
        this.types = types;
        ifconditionFlag = 0;
        startFunctionFlag = 0;
        createVariableFlag = 0;
        className = null;
        methodName = null;
        instancenName = null;
        programCompleted = false;
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
    public Method consume(
            AbstractToken token, Stack<FunctionContext> callStack, StringStack sequenceNumber,
            Set<Node> nodes, Set<Edge> edges, Set<Method> methods)
            throws BuildException{

        FunctionContext currentContext = callStack.peek();

        // see if incorrect number of params are being passed
        if(token.getType() != Instruction.OTHER && anyFlagActive()){
            throw new BuildException("Parameter required not instruction");
        }

        switch(token.getType()){

            // indicates that a variable is being declared
            case DECLARE:
                createVariableFlag = 1;
                break;

            // if an if statement or an elif, the following stmt will be included as a condition
            case BEGIN_IF:
                ifconditionFlag = 1;
                break;

            // if an if statment is ending, pop the condition of the if statement from the condition
            case END_IF:
                ifconditionFlag = 0;
                String discard = currentContext.getIfStack().pop();
                currentContext.getDiscardedIfStack().push(discard);
                break;

            // for an else just negate the previous if
            case BEGIN_ELSE:
                if(ifconditionFlag > 0){
                    throw new BuildException("Previous if statement not ended");
                }
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
                if(createVariableFlag > 0){
                    switch(createVariableFlag++){
                        case 1:
                            className = token.toString();

                            if(!types.contains(className)){
                                throw new BuildException("Undefined class name: " + className);
                            }

                            break;
                        case 2:
                            instancenName = token.toString();
                            //nodes.add(new Node(instancenName));
                            nodes.add(new Node(className, instancenName));
                            createVariableFlag = 0;
                            break;
                    }
                }
                else if(ifconditionFlag > 0){
                    switch(ifconditionFlag++) {
                        case 1:
                            currentContext.getIfStack().push(token.toString());
                            break;
                        default:
                            //TODO
                            break;
                    }
                }
                else if(startFunctionFlag > 0){
                    switch(startFunctionFlag++){
                        case 1:
                            this.className = token.toString();

                            if(nodes.stream().noneMatch(x -> x.getLabel().equals(this.className))){
                                throw new BuildException("Instance does not exist: " + this.className);
                            }
                            break;
                        case 2:
                            this.methodName = token.toString();

                            // create an edge for this call
                            edges.add(new Edge(
                                    this.methodName,                  // the name of the edge
                                    sequenceNumber.toString(".", "", ""), // the seq number
                                    currentContext.getMethod().getClassName(), // the start node
                                    this.className                   // the end node
                            ));

                            // get the type of this instance
                            String type = nodes.stream().filter(x -> x.getLabel().equals(className)).findAny().get().getId();

                            Optional<Method> method = methods.stream().filter(x -> x.getClassName().equals(type) && x.getMethod().equals(methodName)).findAny();

                            if(method.isEmpty()){
                                throw new BuildException("Method does not exist of name: " + methodName);
                            }

                            callStack.push(new FunctionContext(method.get()));
                            startFunctionFlag = 0;
                            return method.get();
                    }
                }
                break;
            case END_PROGRAM:
                if(callStack.size() > 1 || anyFlagActive()){
                    throw new BuildException("Program cannot end");
                }
                programCompleted = true;
                break;
            default:
                throw new BuildException("Invalid token seen after BEGIN_PROGRAM");
        }
        return null;
    }

    public boolean anyFlagActive(){
        return ifconditionFlag == 1 || startFunctionFlag > 0 || createVariableFlag > 0;
    }
}
