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

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Method {

    @EqualsAndHashCode.Include
    private String className;

    @EqualsAndHashCode.Include
    private String method;

    @EqualsAndHashCode.Exclude
    private List<AbstractToken> instructions;
}
