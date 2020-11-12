public class Transition {
    String initialState;
    String literal;
    String nextState;


    public Transition(String initialState, String literal, String nextState) {
        this.initialState = initialState;
        this.literal = literal;
        this.nextState = nextState;
    }


    public String getInitialState() {
        return initialState;
    }

    public void setInitialState(String initialState) {
        this.initialState = initialState;
    }

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    public String getNextState() {
        return nextState;
    }

    public void setNextState(String nextState) {
        this.nextState = nextState;
    }

    public boolean checkStartState(String state){
        return initialState.equals(state);
    }

    @Override
    public String toString() {
        return "Transition{" +
                "initialState='" + initialState + '\'' +
                ", literal='" + literal + '\'' +
                ", nextState='" + nextState + '\'' +
                '}';
    }

    public boolean checkLiteral(String currentLiteral) {
        return literal.equals(currentLiteral);
    }

    public boolean checkFinalState(String state) {
        return nextState.equals(state);
    }
}
