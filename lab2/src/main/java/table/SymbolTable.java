package table;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SymbolTable {

    private static final int MAX_NUMBER_OF_ASCII_CODES = 256;

    private List<List<String>> elements;

    public SymbolTable() {
        elements = new ArrayList<>();
        for(int index = 0; index < MAX_NUMBER_OF_ASCII_CODES; index ++)
            elements.add(new ArrayList<>());
    }


    /*
     * This function returns the position of the token in the symbolTable
     */
    public Pair<Integer, Integer> position(String token) {
        int position = hashCode(token);

        List<String> values = elements.get(position);
        if (!values.contains(token)) {
            values.add(token);
        }
        return new Pair<>(position, values.indexOf(token));
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

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder();

        for(int index= 0; index< elements.size();index++){
            text.append(index + " : " + elements.get(index) + "\n" );
        }

        return text.toString();
    }
}
