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

/**
 * An element on the mock run time stack
 */
@Data
@EqualsAndHashCode
public class FunctionContext {

    // holds the currently active condtions
    @EqualsAndHashCode.Include
    private StringStack ifStack;

    // the functionname for this level of the runtime stack
    @EqualsAndHashCode.Include
    private String functionName;

    // the non active if statements
    @EqualsAndHashCode.Exclude
    private StringStack discardedIfStack;

    // the current sequence number for this call
    @EqualsAndHashCode.Exclude
    private Integer seqNum = 1;
}
