/******************************************************************************
 *
 * BuildException.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.exception;

/**
 * Build Exception
 *
 * @author Ian Laird
 */
public class BuildException extends Throwable {

    /**
     * default constructor
     */
    public BuildException(){
        super();
    }

    /**
     * custom constructor
     * @param s message
     */
    public BuildException(String s){
        super(s);
    }
}
