package CD.util;

import java.util.LinkedList;
import java.util.stream.Collectors;

/**
 * Custom Stack
 * @author Ian Laird
 */
public class Stack {
    private LinkedList<String> stack = new LinkedList<>();
    public String pop(){
       return stack.pop();
    }

    public String peek(){
        return stack.poll();
    }

    public void push(String s){
        stack.push(s);
    }

    public boolean isEmpty(){
        return stack.isEmpty();
    }

    public boolean containsElement(){
        return !isEmpty();
    }

    public String toString(String delim, String start, String stop){
        return this.stack
                .stream()
                .collect(Collectors.joining(delim, start, stop));
    }
}
