import java.util.*;

public class Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        first = new HashMap<>();
        follow = new HashMap<>();

        first();
        follow();
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void parse(String w) {
        //TODO: Implement parse
    }

    private void first() {
        this.grammar.getTerminals().forEach(t -> first.put(t, new HashSet<>(Set.of(t))));
        this.grammar.getNonTerminals().forEach(t -> first.put(t, new HashSet<>()));


        this.grammar.getProductions().entrySet().forEach(p -> {

            final List<List<String>> productions = p.getValue();
            productions.stream().forEach(list -> {
                if (this.grammar.getTerminals().contains(list.get(0)))
                    this.first.get(p.getKey()).add(list.get(0));
            });
        });

        for (String nonterminal : this.grammar.getNonTerminals()) {
            for (List<String> production : this.grammar.getProductionsForNonTerminal(nonterminal)) {
                Set<String> fminus1 = new HashSet<>();
                for (String elementOfProduction : production) {
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
        this.follow.get(this.grammar.getInitialState()).add("epsilon");
        int i = 0;
        boolean changesMade;
        do {
            i++;
            changesMade = false;
            for (String nonterminal : this.grammar.getNonTerminals()) {
                int initialSize = this.follow.get(nonterminal).size();
                for (Map.Entry<String, List<List<String>>> production : this.grammar.getProductions().entrySet()) {
                    for (List<String> list : production.getValue()) {
                        if (list.contains(nonterminal)) {
                            if (list.indexOf(nonterminal) + 1 < list.size()) {
                                String y = list.get(list.indexOf(nonterminal) + 1);
                                for (String element : this.first.get(y))
                                    if (this.first.get(element).contains("epsilon"))
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
        if (a.isEmpty() || a.contains("epsilon")) {
            result.addAll(b);
        }
        return result;
    }
}

