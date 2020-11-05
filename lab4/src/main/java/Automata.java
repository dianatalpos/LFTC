import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automata {

    private List<String> states;
    private List<String> alphabet;
    private String initialState;
    private List<String> finalStates;
    private List<Transition> transitions;

    public Automata() {
        states = new ArrayList<>();
        alphabet = new ArrayList<>();
        finalStates = new ArrayList<>();
        transitions = new ArrayList<>();
    }


    public void readFA() throws IOException, FiniteAutonomaException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\MyApps\\faculta\\lftc\\LFTC\\lab4\\src\\main\\resources\\FA.in"));

        String line = reader.readLine();
        line.strip();

        states.addAll(Arrays.asList(line.split(",")));

        line = reader.readLine();

        alphabet.addAll(Arrays.asList(line.split(",")));

        initialState = reader.readLine();
        initialState.strip();
        if (!states.contains(initialState))
            throw new FiniteAutonomaException("This initial State doesn't exist!");

        line = reader.readLine();
        finalStates.addAll(Arrays.asList(line.split(",")));

        for (String finalState : finalStates) {
            if (!states.contains(finalState))
                throw new FiniteAutonomaException("This final State doesn't exist: " + finalState);
        }

        line = reader.readLine();

        while(line!=null){
            String[] elements = line.split(",");
            final String initial = elements[0];
            final String finalState = elements[2];
            final String literal = elements[1];
            if(!states.contains(initial) || !states.contains(finalState))
                throw new FiniteAutonomaException("State doesn't exist: " + initial + " or "+ finalState);

            if(!alphabet.contains(literal))
                throw new FiniteAutonomaException("Literal doesn't exist: " + literal);

            Transition transition = new Transition(initial, literal, finalState);
            transitions.add(transition);


            line = reader.readLine();

        }
    }


    public void verifySequence(String sequence){
        List<String> literals = new ArrayList<>();
        literals.addAll(Arrays.asList(sequence.split(" ")));


        for(String currentLiteral: literals){
        }
    }

    @Override
    public String toString() {
        return "Automata{" +
                "states=" + states +
                ", alphabet=" + alphabet +
                ", initialState='" + initialState + '\'' +
                ", finalStates=" + finalStates +
                '}';
    }

    public List<String> getStates() {
        return states;
    }

    public List<String> getAlphabet() {
        return alphabet;
    }

    public String getInitialState() {
        return initialState;
    }

    public List<String> getFinalStates() {
        return finalStates;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }
}
