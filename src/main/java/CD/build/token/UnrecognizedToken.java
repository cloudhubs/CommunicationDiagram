/******************************************************************************
 *
 * UnrecognizedToken.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build.token;

import lombok.Data;
import lombok.NonNull;

/**
 * Non instruction token
 *
 * @author Ian laird
 */
@Data
public class UnrecognizedToken implements AbstractToken {

    // holds what the unrecognized token was
    @NonNull
    private String str;

    /**
     * the instruction associated with this type of token is OTHER
     * @return OTHER
     */
    @Override
    public Instruction getType() {
        return Instruction.OTHER;
    }

    /**
     * the token
     * @return unrecognized string
     */
    public String toString(){
        return this.str;
    }
}
