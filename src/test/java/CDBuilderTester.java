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
import edu.baylor.ecs.cloudhubs.prophetdto.communication.Communication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;



@ExtendWith(SpringExtension.class)
public class CDBuilderTester {

    private CDBuilder builder;

    @BeforeEach
    public void before(){
        this.builder = new CDBuilder();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "BEGIN_PROGRAM END_PROGRAM",                    // no type declaration
            "DEFINE_TYPES BEGIN_PROGRAM END_PROGRAM",       // 0 types
            "DEFINE_TYPES foo bar BEGIN_IF END_PROGRAM",    // no BEGIN_PROGRAM
            "DEFINE_TYPES foo bar BEGIN_PROGRAM",           //no END_PROGRAM
            "DEFINE_TYPES foo BEGIN_PROGRAM DECLARE bar foo2 END_PROGRAM", //bar not defined as a valid type
            "DEFINE_TYPES foo DEFINE_TYPES bar BEGIN_PROGRAM, END_PROGRAM", //define types may only be the first instruction
            "DEFINE_TYPES foo bar END_PROGRAM",               // no begin program
            "DEFINE_TYPES foo DEFINE_FUNCTION foo2 bar" +
                    " END_DEFINE_FUNCTION BEGIN_PROGRAM" +
                    " END_PROGRAM", // method is not part of a valid class
            "DEFINE_TYPES foo DEFINE_FUNCTION foo" +
                    " END_DEFINE_FUNCTION BEGIN_PROGRAM" +
                    " END_PROGRAM", // method name is not specified
            "DEFINE_TYPES foo DEFINE_FUNCTION foo bar" +
                    " DEFINE_FUNCTION foo bar2" +
                    " END_DEFINE_FUNCTION END_DEFINE_FUNCTION" +
                    " BEGIN_PROGRAM END_PROGRAM", // cannot nest define funcitons
            "DEFINE_TYPES foo DEFINE_FUNCTION foo" +
                    " bar END_DEFINE_FUNCTION DEFINE_FUNCTION" +
                    " foo bar END_DEFINE_FUNCTION" +
                    " BEGIN_PROGRAM END_PROGRAM", //  overloading is not allowed
            "DEFINE_TYPES foo BEGIN_PROGRAM" +
                    " CALL_FUNCTION END_PROGRAM",
            "DEFINE_TYPES foo BEGIN_PROGRAM" +
                    " CALL_FUNCTION END_FUNCTION_CALL" +
                    " END_PROGRAM",
            "DEFINE_TYPES foo BEGIN_PROGRAM DECLARE" +
                    " foo bar CALL_FUNCTION bar" +
                    " END_FUNCTION_CALL END_PROGRAM",
            "DEFINE_TYPES foo BEGIN_PROGRAM DECLARE" +
                    " foo bar CALL_FUNCTION bar method" +
                    " END_FUNCTION_CALL END_PROGRAM",
            "DEFINE_TYPES foo foo2 DEFINE_FUNCTION" +
                    " foo2 method END_DEFINE_FUNCTION" +
                    " BEGIN_PROGRAM DECLARE foo bar" +
                    " CALL_FUNCTION bar method " +
                    "END_FUNCTION_CALL END_PROGRAM",
            "DEFINE_TYPES foo DEFINE_FUNCTION foo" +
                    " method END_DEFINE_FUNCTION BEGIN_PROGRAM DECLARE" +
                    " foo bar CALL_FUNCTION bar method END_PROGRAM",
            "DEFINE_TYPES foo DEFINE_FUNCTION foo method" +
                    " END_DEFINE_FUNCTION BEGIN_PROGRAM " +
                    "DECLARE foo bar BEGIN_IF CALL_FUNCTION " +
                    "bar method END_IF END_PROGRAM",
            "DEFINE_TYPES foo DEFINE_FUNCTION foo method" +
                    " END_DEFINE_FUNCTION BEGIN_PROGRAM" +
                    " DECLARE foo bar BEGIN_IF X==2" +
                    " CALL_FUNCTION bar method BEGIN_ELSE" +
                    " END_ELSE END_IF END_PROGRAM"
    })
    void testBadString(String s){
        assertThrows(BuildException.class, () -> {
            createCommunication(s);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "DEFINE_TYPES foo BEGIN_PROGRAM END_PROGRAM",
            "DEFINE_TYPES foo bar BEGIN_PROGRAM END_PROGRAM",
            "DEFINE_TYPES foo bar " +
              "DEFINE_FUNCTION foo method DECLARE bar exampleObject" +
                    " CALL_FUNCTION exampleObject getDoc END_FUNCTION_CALL" +
                    " END_DEFINE_FUNCTION " +
              "DEFINE_FUNCTION bar getDoc END_DEFINE_FUNCTION " +
              "BEGIN_PROGRAM DECLARE foo o CALL_FUNCTION o method END_FUNCTION_CALL END_PROGRAM"
    })
    public void testGoodString(String s){
        assertDoesNotThrow(() -> {
            Communication diagram = createCommunication(s);
            diagram.getEdges();
        });
    }

    public Communication createCommunication(String s) throws BuildException{
        return this.builder.consume(s).build();
    }
}
