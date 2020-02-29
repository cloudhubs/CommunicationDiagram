/******************************************************************************
 *
 * Method.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build;

import CD.build.token.AbstractToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * Represents a method in the language
 *
 * @author Ian Laird
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Method {

    // the class name
    @EqualsAndHashCode.Include
    private String className;

    // the method name
    @EqualsAndHashCode.Include
    private String method;

    // each instruction associated with this Method in order
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<AbstractToken> instructions;
}
