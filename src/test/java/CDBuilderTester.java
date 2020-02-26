/******************************************************************************
 *
 * CDBuilderTester.java
 *
 * author: Ian laird
 *
 * Created 2/26/20
 *
 * © 2020
 *
 ******************************************************************************/

import CD.build.CDBuilder;
import CD.exception.BuildException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.assertThrows;



@ExtendWith(SpringExtension.class)
public class CDBuilderTester {

    @ParameterizedTest
    @ValueSource(strings = {
            "BEGIN_PROGRAM END_PROGRAM",                    // no type declaration
            "DEFINE_TYPES BEGIN_PROGRAM END_PROGRAM",       // 0 types
            "DEFINE_TYPES foo bar BEGIN_IF END_PROGRAM",    // no BEGIN_PROGRAM
            "DEFINE_TYPES foo bar BEGIN_PROGRAM",           //no END_PROGRAM
            "DEFINE_TYPES foo BEGIN_PROGRAM DECLARE bar foo2 END_PROGRAM", //bar not defined as a valid type
            "DEFINE_TYPES foo DEFINE_TYPES bar BEGIN_PROGRAM, END_PROGRAM", //define types may only be the first instruction
            "DEFINE_TYPES foo bar END_PROGRAM"               // no begin program
    })
    void testBadString(String s){
        CDBuilder builder = new CDBuilder();
        assertThrows(BuildException.class, () -> {
            builder.consume(s);
            builder.build();
        });
    }
}
