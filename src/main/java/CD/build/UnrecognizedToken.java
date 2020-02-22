package CD.build;

import lombok.Data;

@Data
public class UnrecognizedToken implements AbstractToken {
    private String str;

    @Override
    public Instruction getType() {
        return Instruction.OTHER;
    }

    public String toString(){
        return this.str;
    }
}
