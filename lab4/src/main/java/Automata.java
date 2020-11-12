import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

        while (line != null) {
            String[] elements = line.split(",");
            final String initial = elements[0];
            final String finalState = elements[2];
            final String literal = elements[1];
            if (!states.contains(initial) || !states.contains(finalState))
                throw new FiniteAutonomaException("State doesn't exist: " + initial + " or " + finalState);

            if (!alphabet.contains(literal))
                throw new FiniteAutonomaException("Literal doesn't exist: " + literal);

            Transition transition = new Transition(initial, literal, finalState);
            transitions.add(transition);


            line = reader.readLine();

        }
    }


    public void verifySequence(String sequence) throws FiniteAutonomaException {
        List<String> literals = new ArrayList<>();
        literals.addAll(Arrays.asList(sequence.split(" ")));
        List<Transition> currentTransitions = transitions;
        List<String> currentStates = new ArrayList<>();

        currentStates.add(initialState);

        for(int i = 0; i<literals.size(); i++)
        {
            String currentLiteral = literals.get(i);
            if (alphabet.contains(currentLiteral)) {

                List<String> finalCurrentStates = currentStates;

                currentTransitions = currentTransitions.stream()
                        .filter(transition -> {
                            for(String state: finalCurrentStates){
                                if(transition.checkStartState(state) && transition.checkLiteral(currentLiteral))
                                    return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());

                List<String> finalCurrentStates1 =currentTransitions.stream()
                        .map(transition -> transition.getNextState())
                        .collect(Collectors.toList());

                currentTransitions = transitions.stream()
                        .filter(transition -> {
                            for(String state: finalCurrentStates1){
                                if(transition.checkStartState(state))
                                    return true;
                            }
                            return false;
                        })
                        .collect(Collectors.toList());

                currentStates = finalCurrentStates1;

                if(i != literals.size()-1 && currentTransitions.isEmpty())
                    throw new FiniteAutonomaException("Sequence not accepted!");

            } else
                throw new FiniteAutonomaException("Sequence has wrong literals!");

        }

        for(String state : currentStates)
        {
            for(String finalState: finalStates)
                if(state.equals(finalState))
                    System.out.println("Sequence accepted!");
                    return;
        }

        System.out.println("Sequence not accepted!");

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
