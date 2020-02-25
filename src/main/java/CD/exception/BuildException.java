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

public class BuildException extends Throwable {
    public BuildException(){
        super();
    }

    public BuildException(String s){
        super(s);
    }
}
