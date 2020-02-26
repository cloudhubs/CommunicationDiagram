/******************************************************************************
 *
 * AbstractToken.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build.token;

import CD.exception.BuildException;

import static CD.build.token.Instruction.*;

/**
 * Represents a Token
 *
 * @author Ian laird
 */
public interface AbstractToken {

    /**
     * gets the type of the token
     * @return type
     */
    public Instruction getType();


    /**
     * gets the string rep of the token
     * @return string
     */
    public String toString();

    public static AbstractToken getTokenFromStr(String str) throws BuildException {
        switch(str){
            case "DEFINE_TYPES":
                return DEFINE_TYPES;
            case "DEFINE_FUNC":
                return DEFINE_FUNC;
            case "END_DEFINE_FUNC":
                return END_DEFINE_FUNC;
            case "DECLARE":
                return DECLARE;
            case "CALL_FUNCTION":
                return CALL_FUNCTION;
            case "END_FUNCTION_CALL":
                return END_FUNCTION_CALL;
            case "BEGIN_IF":
                return BEGIN_IF;
            case "END_IF":
                return END_IF;
            case "BEGIN_ELSE":
                return BEGIN_ELSE;
            case "END_ELSE":
                return END_ELSE;
            case "BEGIN_PROGRAM":
                return BEGIN_PROGRAM;
            case "END_PROGRAM":
                return END_PROGRAM;
            default:
                return new UnrecognizedToken(str);
        }
    }
}
