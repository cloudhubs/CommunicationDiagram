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

@Data
public class UnrecognizedToken implements AbstractToken {
    private String str;

    @Override
    public Instruction getType() {
        return Instruction.OTHER;
    }

    public String toString(){
        return this.str;
    }
}
