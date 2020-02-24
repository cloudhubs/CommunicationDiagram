package CD.build;

import CD.exception.BuildException;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static CD.build.Instruction.DEFINE_FUNC;
import static CD.build.Instruction.END_DEFINE_FUNC;

public class HeaderInterpretor {

    private List<AbstractToken> arguments = new LinkedList<>();
    private boolean firstSeen = true;
    private Instruction lastEncounteredInstruction = null;
    private boolean program_begun = false;

    private Set<String> types = new HashSet<>();
    private Set<Method> methods = new HashSet<>();


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
    public boolean consumeBeforeStart(AbstractToken token) throws BuildException{
        if(firstSeen){
            if(token.getType() != Instruction.DEFINE_TYPES){
                throw new BuildException("First instruction must be DEFINE_TYPES");
            }
        }
        firstSeen = false;

        if((token.getType() == Instruction.OTHER) || ((lastEncounteredInstruction == DEFINE_FUNC) && (token.getType() != END_DEFINE_FUNC))){
            arguments.add(token);
            return this.program_begun;
        }

        // if not an argument it is another command to execute
        // therefore execute the last command
        switch(lastEncounteredInstruction){

            // for DEFINE_TYPES each argument is a type
            case DEFINE_TYPES:
                arguments.forEach(x -> types.add(x.toString()));
                break;

            // definition of a function
            case DEFINE_FUNC: //TODO
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
        return this.program_begun;
    }

    public Set<String> getTypes() {
        return types;
    }

    public Set<Method> getMethods() {
        return methods;
    }
}
