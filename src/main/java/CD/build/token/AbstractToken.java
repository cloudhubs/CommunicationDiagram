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
}
