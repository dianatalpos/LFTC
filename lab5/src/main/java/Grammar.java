
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Grammar {
    Set<String> nonTerminals;
    Set<String> terminals;
    String initialState;
    Map<String, List<Pair<Integer, List<String>>>> productions;

    public Grammar() {
        this.nonTerminals = new HashSet<>();
        this.terminals = new HashSet<>();
        this.initialState = "";
        this.productions = new HashMap<>();

        readFromFile("src//main//resources//test2.txt");
    }

    public void readFromFile(String filename) {
        BufferedReader reader;
        int i = 1;
        try {
            reader = new BufferedReader(new FileReader(filename));
            this.nonTerminals = new HashSet<>(Arrays.asList(reader.readLine().split(" ")));
            this.terminals = new HashSet<>(Arrays.asList(reader.readLine().split(" ")));
            this.initialState = reader.readLine().trim();

            String line = reader.readLine();
            this.productions = new HashMap<>();
            for (String nonTerminal : this.nonTerminals) {
                this.productions.put(nonTerminal, new ArrayList<>());
            }

            while (line != null) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, "->");
                String symbol = stringTokenizer.nextToken().trim();
                while (stringTokenizer.hasMoreTokens()) {
                    final String allElementsProduction = stringTokenizer.nextToken().trim();
                    ArrayList productionElements = new ArrayList();
                    productionElements.addAll(Arrays.asList(allElementsProduction.split(" ")));

                    Pair element = new Pair(i, productionElements);
                    this.productions.get(symbol).add(element);
                    i++;
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public String getInitialState() {
        return initialState;
    }

    public Map<String, List<Pair<Integer, List<String>>>> getProductions() {
        return productions;
    }

    public List<Pair<Integer, List<String>>> getProductionsForNonTerminal(String nonTerminal) {
        return productions.get(nonTerminal);
    }
}