package CD.build;

import CD.util.StringStack;

import java.util.Stack;

/******************************************************************************
 *
 * CallStack.java
 *
 * author: Ian laird
 *
 * Created 3/2/20
 *
 * © 2020
 *
 ******************************************************************************/
public class CallStack {

    private Stack<FunctionContext> stack;
    private StringStack sequenceNumber;

    public CallStack(){
        stack = new Stack<>();
        sequenceNumber = new StringStack();
    }

    public void push(FunctionContext functionContext){
        stack.push(functionContext);
        sequenceNumber.push("1");
    }

    public FunctionContext peek(){
        return stack.peek();
    }

    public FunctionContext pop(){
        sequenceNumber.pop();
        Integer toIncrement = Integer.parseInt(sequenceNumber.pop()) + 1;
        sequenceNumber.push(toIncrement.toString());
        return stack.pop();
    }

    public String getSequenceNumber(){
        return sequenceNumber.toString(".", "", "");
    }

    public int size(){
        return stack.size();
    }
}
