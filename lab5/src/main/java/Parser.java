import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private static final String EPSILON = "epsilon";
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;
    private Map<Pair<String, String>, Pair<Integer, List<String>>> table;
    Map<Derivation, Derivation> derivationTable;
    List<Pair<String, String>> conflicts;

    public Parser(Grammar grammar) throws ConflictException {
        this.grammar = grammar;
        first = new HashMap<>();
        follow = new HashMap<>();
        table = new HashMap<>();
        derivationTable = new HashMap<>();


        first();
        follow();
        buildTable();
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public Map<Derivation, Derivation> parse(String w) {
        derivationTable = new HashMap<>();

        String[] elements = w.split(" ");

        Stack<String> alpha = new Stack();
        alpha.push("$");
        for (int i = elements.length - 1; i >= 0; i--)
            alpha.push(String.valueOf(elements[i]));


        Stack<String> beta = new Stack();
        beta.push("$");
        beta.push(grammar.initialState);

        List pi = new ArrayList();

        Derivation currentDerivation =  new Derivation(alpha, beta, pi);
        derivationTable.put(new Derivation(), currentDerivation);

        boolean finish = false;
        while (!finish) {

            String alphaLiteral = alpha.peek();
            String betaLiteral = beta.peek();


            if (betaLiteral.equals(EPSILON)) {
                beta.pop();
            }
            else {
                Pair<Integer, List<String>> pair = this.table.get(new Pair(betaLiteral, alphaLiteral));
                if (pair.element1 != null && pair.element1 != -1) {
                    beta.pop();
                    for (int i = pair.element2.size() - 1; i >= 0; i--) {
                        beta.push(pair.element2.get(i));
                    }
                    pi.add(pair.element1);
                } else {
                    if (pair.element2 != null && pair.element2.get(0).equals("pop")) {
                        beta.pop();
                        alpha.pop();
                    } else if (pair.element2 != null && pair.element2.get(0).equals("acc")) {
                        System.out.println("Sequence accepted");
                        derivationTable.put(new Derivation(alpha, beta, pi), new Derivation());
                        finish = true;
                    } else {
                        System.out.println("Sequence not accepted");
                        finish = true;
                    }

                }
                Derivation nextDerivation =  new Derivation(alpha, beta, pi);
                derivationTable.put(currentDerivation, nextDerivation);
                currentDerivation = nextDerivation;
            }
        }


        Derivation current = derivationTable.get(new Derivation());
        while(current!=null && current.isNotEmpty()){
            System.out.println(current);
            current = derivationTable.get(current);
        }

        return derivationTable;
    }

    private void buildTable() throws ConflictException {
        this.grammar.getNonTerminals().forEach(
                nonTerminal -> {
                    this.grammar.getTerminals().forEach(terminal -> {
                        if (terminal.equals(EPSILON))
                            table.put(new Pair<>(nonTerminal, "$"), new Pair());
                        else
                            table.put(new Pair<>(nonTerminal, terminal), new Pair());
                    });
                }
        );

        this.grammar.getTerminals().forEach(
                terminal -> {
                    if (terminal.equals(EPSILON)) {
                        this.grammar.getTerminals().forEach(terminal2 -> {
                            if (terminal2.equals(EPSILON))
                                table.put(new Pair<>("$", "$"), new Pair(-1, Arrays.asList("acc")));
                            else
                                table.put(new Pair<>("$", terminal2), new Pair());
                        });
                    } else {
                        this.grammar.getTerminals().forEach(terminal2 -> {
                            if (terminal2.equals(EPSILON))
                                table.put(new Pair<>(terminal, "$"), new Pair());
                            else {
                                if (terminal.equals(terminal2))
                                    table.put(new Pair<>(terminal, terminal2), new Pair(-1, Arrays.asList("pop")));
                                else table.put(new Pair<>(terminal, terminal2), new Pair());
                            }
                        });
                    }
                }
        );


        for (String nonTerminal : this.grammar.getNonTerminals()) {
            Set<String> firstOfNonTerminal = this.first.get(nonTerminal);

            List<Pair<Integer, List<String>>> productionsOfNonTerminal = this.grammar.getProductionsForNonTerminal(nonTerminal);

            for (String terminal : firstOfNonTerminal) {
                if (terminal.equals(EPSILON)) {

                    List<String> followOfTerminal = this.follow.get(nonTerminal)
                            .stream().collect(Collectors.toList());

                    for (int i = 0; i < followOfTerminal.size(); i++) {
                        Pair key;
                        if (followOfTerminal.get(i).equals(EPSILON))
                            key = new Pair(nonTerminal, "$");
                        else
                            key = new Pair(nonTerminal, followOfTerminal.get(i));

                        Pair value = new Pair();
                        for (Pair<Integer, List<String>> element : productionsOfNonTerminal) {
                            if (element.element2.contains(EPSILON)) {
                                value = new Pair(element.element1, element.element2);
                            }
                        }
                        Pair<Integer, List<String>> pair = this.table.get(key);
//                        if(pair.element1!=null && pair.element2 != null)
//                            throw new ConflictException("Conflict found: " + pair + " : " + key + "  " + value);
                       this.table.put(key, value);
                    }
                } else {
                    List<Pair<Integer, List<String>>> prod = this.grammar.getProductionsForNonTerminal(nonTerminal);
                    final Pair<String, String> key = new Pair<>(nonTerminal, terminal);
                    if (prod.size() == 1) {
                        Pair pair2 = this.table.get(key);
//                        if(pair2.element1!=null && pair2.element2 != null)
//                            throw new ConflictException("Conflict found: " + pair2 + " : " + key + "  " +  pair2);
//
                        pair2.element2 = prod.get(0).element2;
                        pair2.element1 = prod.get(0).element1;

                        this.table.put(key, pair2);
                    } else {
                        Pair value = new Pair();
                        for (Pair<Integer, List<String>> element : productionsOfNonTerminal) {
                            if (element.element2.contains(terminal)) {
                                value.element1 = element.element1;
                                value.element2 = element.element2;
                            }
                        }
                        Pair<Integer, List<String>> pair = this.table.get(key);
//                        if(pair.element1!=null && pair.element2 != null)
//                            throw new ConflictException("Conflict found: " + pair + " : " + key + "  " +  value);

                        this.table.put(key, value);
                    }
                }
            }

        }
    }


    private void first() {
        this.grammar.getTerminals().forEach(t -> first.put(t, new HashSet<>(Set.of(t))));
        this.grammar.getNonTerminals().forEach(t -> first.put(t, new HashSet<>()));


        this.grammar.getProductions().entrySet().forEach(p -> {

            final List<Pair<Integer, List<String>>> productions = p.getValue();
            productions.stream().forEach(list -> {
                if (this.grammar.getTerminals().contains(list.element2.get(0)))
                    this.first.get(p.getKey()).add(list.element2.get(0));
            });
        });

        for (String nonterminal : this.grammar.getNonTerminals()) {
            for (Pair<Integer, List<String>> production : this.grammar.getProductionsForNonTerminal(nonterminal)) {
                Set<String> fminus1 = new HashSet<>();
                for (String elementOfProduction : production.element2) {
                    if (this.first.get(elementOfProduction).size() == 0) {
                        break;
                    }
                    fminus1.addAll(concatenate(fminus1, this.first.get(elementOfProduction)));
                }
                first.get(nonterminal).addAll(fminus1);
            }
        }

        System.out.println(first);
    }


    private void follow() {
        this.grammar.getNonTerminals().forEach(t -> follow.put(t, new HashSet<>()));
        this.follow.get(this.grammar.getInitialState()).add(EPSILON);
        int i = 0;
        boolean changesMade;
        do {
            i++;
            changesMade = false;
            for (String nonterminal : this.grammar.getNonTerminals()) {
                int initialSize = this.follow.get(nonterminal).size();
                for (Map.Entry<String, List<Pair<Integer, List<String>>>> production : this.grammar.getProductions().entrySet()) {
                    for (Pair<Integer, List<String>> pair : production.getValue()) {
                        if (pair.element2.contains(nonterminal)) {
                            if (pair.element2.indexOf(nonterminal) + 1 < pair.element2.size()) {
                                String y = pair.element2.get(pair.element2.indexOf(nonterminal) + 1);
                                for (String element : this.first.get(y))
                                    if (this.first.get(element).contains(EPSILON))
                                        this.follow.get(nonterminal).addAll(this.follow.get(production.getKey()));
                                    else
                                        this.follow.get(nonterminal).addAll(this.first.get(element));
                            } else
                                this.follow.get(nonterminal).addAll(this.follow.get(production.getKey()));
                        }
                    }
                }
                if (initialSize != this.follow.get(nonterminal).size())
                    changesMade = true;
            }
        } while (changesMade);
        System.out.println(follow);
    }

    private Set<String> concatenate(Set<String> a, Set<String> b) {
        Set<String> result = new HashSet<>(a);
        if (a.isEmpty() || a.contains(EPSILON)) {
            result.addAll(b);
        }
        return result;
    }

    public void printTable() {
        table.entrySet().stream()
                .forEach(System.out::println);
    }

    public void printTable(String filename) throws IOException {
        FileWriter printer = new FileWriter(filename);

        Derivation current = derivationTable.get(new Derivation());
        while(current!=null && current.isNotEmpty()){
            printer.append(current.toString() + "\n");
            current = derivationTable.get(current);
        }

        printer.close();

    }
}

