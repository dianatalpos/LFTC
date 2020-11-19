import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class Parser {
    private Grammar grammar;
    private Map<String, Set<String>> first;
    private Map<String, Set<String>> follow;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        first = new HashMap<>();
        follow = new HashMap<>();
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public void parse(String w) {
        //TODO: Implement parse
    }

    private void first() {
        /// add the terminals
        this.grammar.getTerminals().forEach(terminal -> first.put(terminal, Set.of(terminal)));
        /// add the non terminals
        this.grammar.getNonTerminals().forEach(nonTerminal -> first.put(nonTerminal, new HashSet<>()));

        ///initialization
        this.grammar.getProductions().entrySet().forEach(production -> {
            /// we add the first terminal of each production to the non-terminal first array
            if (this.grammar.getTerminals().contains(production.getValue().get(0)))
                this.first.get(production.getKey()).add(production.getValue().get(0));
        });

        //TODO: Implement
    }


    private void follow() {
        //TODO: implement
    }

}
