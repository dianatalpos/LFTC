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

    @Override
    public String toString() {
        return "Transition{" +
                "initialState='" + initialState + '\'' +
                ", literal='" + literal + '\'' +
                ", nextState='" + nextState + '\'' +
                '}';
    }
}
