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

import java.util.*;

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
    public boolean program_begun = false;

    // holds all defined types
    private Set<String> types = new HashSet<>();

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
    public Method consume(AbstractToken token) throws BuildException{

        Method toReturn = null;

        if(firstSeen && token.getType() == DEFINE_TYPES){
            firstSeen = false;
            this.program_begun = false;
            return toReturn;
        }else if((firstSeen && token.getType() != DEFINE_TYPES) || ( !firstSeen && token.getType() == DEFINE_TYPES)){
            throw new BuildException("DEFINE_TYPES must be the first instruction and none other");
        }

        // if this is not an instruction or this instruction is nested within a method declaration save it as an argument
        if((token.getType() == Instruction.OTHER) || ((lastEncounteredInstruction == DEFINE_FUNCTION) && (token.getType() != END_DEFINE_FUNCTION))){
            if(token.getType() == DEFINE_FUNCTION){
                throw new BuildException("Cannot have nested DEFINE_FUNCTION");
            }
            arguments.add(token);
            return toReturn;
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
            case DEFINE_FUNCTION:
                if(this.arguments.size() < 2){
                    throw new BuildException("usage: DEFINE_FUNC classname methodName [instruction...]");
                }
                String ownerClass = this.arguments.get(METHOD_CLASS_POS).toString();
                if(!types.contains(ownerClass)){
                    throw new BuildException("Class does not exist: " + ownerClass);
                }

                String methodName = this.arguments.get(METHOD_NAME_POS).toString();

                List<AbstractToken> instructions = new ArrayList<>(this.arguments.subList(METHOD_FIRST_INSTRUCTION_POS, this.arguments.size()));
                toReturn = new Method(ownerClass, methodName, instructions);
                break;

            case END_DEFINE_FUNCTION:
                break;

            case BEGIN_PROGRAM:
                break;

            default:
                throw new BuildException("Invalid instruction before BEGIN_PROGRAM");

        }

        if(token.getType() == BEGIN_PROGRAM){
            this.program_begun = true;
        }
        lastEncounteredInstruction = token.getType();

        // clear out the saved tokens
        this.arguments.clear();

        return toReturn;
    }

    public Set<String> getTypes() {
        return types;
    }

}
