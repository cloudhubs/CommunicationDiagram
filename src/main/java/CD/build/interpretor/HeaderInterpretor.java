/******************************************************************************
 *
 * HeaderInterprator.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build.interpretor;

import CD.build.token.AbstractToken;
import CD.build.token.Instruction;
import CD.build.Method;
import CD.exception.BuildException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static CD.build.token.Instruction.*;

/**
 * Consumes Header tokens
 *
 * @author Ian Laird
 */
public class HeaderInterpretor {

    // static variables ///////////////////////////////////////////////

    // the class name that hold the method should be the first argument
    private static final int METHOD_CLASS_POS = 0;

    // the method name should be the second argument
    private static final int METHOD_NAME_POS = 1;

    // the first instruction of the method will be the third token following a DEFINE_FUNCTION command
    private static final int METHOD_FIRST_INSTRUCTION_POS = 2;

    // class variables ////////////////////////////////////////////////////

    // the tokens seen since the last recognized instruction
    private List<AbstractToken> arguments = new LinkedList<>();

    // set only if no tokens have been read in so far
    private boolean firstSeen = true;

    // the last encountered instruction
    private Instruction lastEncounteredInstruction = DEFINE_TYPES;

    // set indicates that BEGIN_PROGRAM has been seen
    private boolean program_begun = false;

    // holds all defined types
    private Set<String> types = new HashSet<>();

    // holds all defined methods
    private Set<Method> methods = new HashSet<>();

    //  methods  ////////////////////////////////////////////////////////////

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
    public boolean consume(AbstractToken token) throws BuildException{

        if((firstSeen && token.getType() != DEFINE_TYPES) || (token.getType() == DEFINE_TYPES)){
            throw new BuildException("DEFINE_TYPES must be the first instruction and none other");
        }
        firstSeen = false;

        // if this is not an instruction or this instruction is nested within a method declaration save it as an argument
        if((token.getType() == Instruction.OTHER) || ((lastEncounteredInstruction == DEFINE_FUNC) && (token.getType() != END_DEFINE_FUNC))){
            arguments.add(token);
            return this.program_begun;
        }

        // if this token is an instruction, execute the last instruction
        switch(lastEncounteredInstruction){

            // for DEFINE_TYPES each argument is a type
            case DEFINE_TYPES:
                if(arguments.isEmpty()){
                    throw new BuildException("At least one type must be defined");
                }
                arguments.forEach(x -> types.add(x.toString()));
                break;

            // definition of a function
            case DEFINE_FUNC:
                if(this.arguments.size() < 2){
                    throw new BuildException("usage: DEFINE_FUNC classname methodName [instruction...]");
                }
                String ownerClass = this.arguments.get(METHOD_CLASS_POS).toString();
                String methodName = this.arguments.get(METHOD_NAME_POS).toString();
                List<AbstractToken> instructions = this.arguments.subList(METHOD_FIRST_INSTRUCTION_POS, this.arguments.size());
                Method method = new Method(ownerClass, methodName, instructions);
                this.methods.add(method);
                break;

            case END_DEFINE_FUNC:
                break;

            case BEGIN_PROGRAM:
                this.program_begun = true;
                break;

            default:
                throw new BuildException("Invalid instruction before BEGIN_PROGRAM");

        }
        lastEncounteredInstruction = token.getType();

        // clear out the saved tokens
        this.arguments.clear();

        return this.program_begun;
    }

    public Set<String> getTypes() {
        return types;
    }

    public Set<Method> getMethods() {
        return methods;
    }
}
