/******************************************************************************
 *
 * Instruction.java
 *
 * author: Ian laird
 *
 * © 2020 CloudHubs
 *
 ******************************************************************************/

package CD.build.token;

/**
 * An implementation of a token that represents an instruction.
 *
 * @author Ian Laird
 */
public enum Instruction implements AbstractToken {

        DEFINE_TYPES("DEFINE_TYPES"),
        DEFINE_FUNC("DEFINE_FUNC"),
        END_DEFINE_FUNC("END_DEFINE_FUNC"),
        DECLARE("DECLARE"),
        CALL_FUNCTION("CALL_FUNCTION"),
        END_FUNCTION_CALL("END_FUNCTION_CALL"),
        BEGIN_IF("BEGIN_IF"),
        END_IF("END_IF"),
        BEGIN_ELSE("START_ELSE"),
        END_ELSE("END_ELSE"),
        BEGIN_PROGRAM("BEGIN_PROGRAM"),
        END_PROGRAM("END_PROGRAM"),
        OTHER("OTHER");

        // holds the string representation of the enum
        private String str;

        /**
         * custom constructor
         * @param str the init string
         */
        Instruction(String str) {
                this.str = str;
        }

        /**
         * returns the instruction type
         * @return instruction enum
         */
        @Override
        public Instruction getType() {
                return this;
        }

        /**
         * string rep of instruction
         * @return str
         */
        @Override
        public String toString() {
                return this.str;
        }
}
