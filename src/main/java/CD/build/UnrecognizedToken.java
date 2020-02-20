package CD.build;

import lombok.Data;

@Data
public class UnrecognizedToken implements AbstractToken {
    private String str;

    @Override
    public CD_Token getType() {
        return CD_Token.OTHER;
    }

    public String toString(){
        return this.str;
    }
}
