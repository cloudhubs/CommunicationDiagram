/******************************************************************************
 *
 * CDBuilder.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/


package CD.build;

import CD.build.interpretor.HeaderInterpretor;
import CD.build.interpretor.ProgramInterpretor;
import CD.build.token.AbstractToken;
import CD.exception.BuildException;
import CD.util.StringStack;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Edge;
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Node;

import java.util.*;

/**
 * CDBuilder
 *
 * This class is used to build a Communication Diagram. The idea is that you feed tokens to
 * the builder one at a time and after the last token is entered call build. This will
 * construct the diagram. I have created a custom but simple language for interacting with this builder
 * which I will outline below. In order to create a CD Diagram for a program the source code needs
 * to be translated to this custom language so that it can be analyzed.
 *
 * Language Overview:
 *
 * The language starts with the header section which is where the types and functions are declared.
 * Following this section is the program section.
 *
 * Each token is analyzed and keywords are classified as instructions.
 * Naturally these instructions are reserved.
 *
 * The program is executed by running one token at a time and storing the state. The way that functions
 * are handled is that when they are defined their tokens are stored in order with an associated name.
 * When the function is called, each associated token is called one by one.
 *
 * Instructions:
 *
 *    Header Instructions are as follows. Only valid before BEGIN_PROGRAM is seen.
 *
 *         DEFINE_TYPES: all non instruction tokens after this are considered class names
 *            this must be the first instruction in the program
 *
 *         DEFINE_FUNC: creating a function definition
 *            all tokens after this are stored until END_DEFINE_FUNC
 *
 *         END_DEFINE_FUNC: ends a function definition
 *
 *         BEGIN_PROGRAM: ends header section and begins the program section
 *
 *    Program instructions are as follows. Only valid after BEGIN_PROGRAM is seen:
 *
 *        DECLARE: declares an instance of an object.
 *            params: type name
 *
 *        START_IF: begins conditional execution of the following code
 *            the next token is the condition
 *
 *        END_IF: ends last seen START_IF
 *
 *        START_ELSE: begins a negation of the last if block
 *
 *        END_ELSE: ends an else block
 *
 *        CALL_FUNCTION: indicates that a function is going to be called
 *            params: objectName functionName END_FUNCTION_CALL
 *
 *        END_FUNCTION_CALL: indicates the function call is over
 *            MUST BE PLACED AFTER A methodName that is being called
 *            I will probably take this instruction out later but it makes parsing easier
 *
 *        END_PROGRAM: ends a program
 *            must be the last token in the program
 *
 *
 * @author Ian Laird
 */
public class CDBuilder {

    // used to interpret tokens once the program has begun executing
    private ProgramInterpretor programInterpretor = null;

    // used to interpret the header of the program
    private HeaderInterpretor headerInterpretor = null;

    // meant to mimic the runtime stack
    private Stack<FunctionContext> callStack = null;

    // used to create sequence numbers of communications
    private StringStack sequenceNumber = null;

    // the types of objects that are defined for this program
    private Set<String> types = null;

    // the methods defined for all objects in this program
    private Set<Method> methods = null;

    // the nodes for the communication diagram
    private Set<Node> nodes = null;

    // the edges for the communication diagram
    private Set<Edge> edges = null;

    // indicates that the program has begun executing
    private boolean program_begun = false;

    // indicates that the program has finished executing
    private boolean program_terminated = false;

    public CDBuilder(){
        Method main = new Method("this", "main", null);

        headerInterpretor = new HeaderInterpretor();
        callStack = new Stack<>();
        callStack.add(new FunctionContext(main));
        sequenceNumber = new StringStack();
        types = new HashSet<>();
        methods = new HashSet<>();
        methods.add(main);
        nodes = new HashSet<>();
        edges = new HashSet<>();
        programInterpretor = null;
    }

    /**
     * consumes a string by tokenizing by white space
     * @param str the string to tokenize
     * @throws BuildException
     */
    public CDBuilder consume(String str) throws BuildException{
        String [] tokens = str.split("\\s+");
        for (String s : tokens){
            consume(AbstractToken.getTokenFromStr(s));
        }
        return this;
    }

    /**
     * consumes a token
     *
     * @param token
     */
    public CDBuilder consume(AbstractToken token) throws BuildException{

        // if the END_PROGRAM token has been encountered no more tokens are allowed
        if(program_terminated){
            throw new BuildException("END_PROGRAM already encountered");
        }

        // if still in the header phase feed the token to the header interprator
        if(!program_begun){
            Method m = headerInterpretor.consume(token);
            program_begun = headerInterpretor.program_begun;

            if(Objects.nonNull(m)){
                if(!this.methods.add(m)){
                    throw new BuildException("Method already declared: " + m);
                }
            }

            // if this token began the program get the necessary results
            if(headerInterpretor.program_begun){
                this.types = headerInterpretor.getTypes();
                programInterpretor = new ProgramInterpretor(types);
            }
        }

        // if in program phase feed the tokens to the program interpretor
        else{
            Method m = programInterpretor.consume(token, callStack, sequenceNumber, nodes, edges, methods);
            this.program_terminated = programInterpretor.programCompleted;
            if(Objects.nonNull(m)){
                for(AbstractToken mToken : m.getInstructions()){
                    consume(mToken);
                }
            }
        }
        return this;
    }


    /**
     * builds the {@link Communication}
     *
     * @return the Communication Diagram corresponding to the tokens that have been fed to this builder
     * @throws BuildException if a communication diagram is unable to be built
     */
    public Communication build() throws BuildException {
        if(!this.program_terminated){
            throw new BuildException("END_PROGRAM not encountered");
        }
        return new Communication(this.nodes, this.edges);
    }
}
