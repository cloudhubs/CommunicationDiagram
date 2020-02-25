/******************************************************************************
 *
 * FunctionContext.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build;

import CD.util.StringStack;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class FunctionContext {

    @EqualsAndHashCode.Include
    private StringStack ifStack;

    @EqualsAndHashCode.Include
    private String functionName;

    @EqualsAndHashCode.Exclude
    private StringStack discardedIfStack;

    @EqualsAndHashCode.Exclude
    private Integer seqNum = 1;
}
