package table;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    private static final int MAX_NUMBER_OF_ASCII_CODES = 256;

    private List<List<String>> elements;


    /*
     * This function returns the position of the token in the symbolTable
     */
    public Map.Entry<Integer, Integer> position(String token) {
        int position = hashCode(token);

        List<String> values = elements.get(position);
        if (!values.contains(token)) {
            values.add(token);
        }
        return new AbstractMap.SimpleEntry(position, values.indexOf(token));
    }


    /*
     * This function return the position of the token in the hashtable
     */
    private int hashCode(String token) {
        long sum = 0;
        for (int index = 0; index < token.length(); index++) {
            sum += token.charAt(index);
        }
        return (int) (sum % MAX_NUMBER_OF_ASCII_CODES);
    }
}
