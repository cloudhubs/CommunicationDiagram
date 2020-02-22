package CD.build;

public enum Instruction implements AbstractToken {

        DEFINE_TYPES("DEFINE_TYPES"), DEFINE_FUNC("DEFINE_FUNC"), END_DEFINE_FUNC("END_DEFINE_FUNC"), DECLARE("DECLARE"), CALL_FUNCTION("CALL_FUNCTION"), BEGIN_IF("BEGIN_IF"), END_IF("END_IF"), BEGIN_HEADER("BEGIN_HEADER"), END_HEADER("END_HEADER"), OTHER("OTHER");

        private String str;

        Instruction(String str) {
                this.str = str;
        }

        @Override
        public Instruction getType() {
                return this;
        }

        @Override
        public String toString() {
                return this.str;
        }
}
