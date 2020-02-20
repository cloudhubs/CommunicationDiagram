package CD.build;

public enum CD_Token implements AbstractToken {

        START_IF("START_IF"), START_ELSE("START_ELSE"), END_IF("END_IF"), END_ELSE("END_ELSE"), START_FUNCTION("START_FUNCTION"), END_FUNCTION("END_FUNCTION"), OTHER("OTHER");

        private String str;

        CD_Token(String str) {
                this.str = str;
        }

        @Override
        public CD_Token getType() {
                return this;
        }

        @Override
        public String toString() {
                return this.str;
        }
}
