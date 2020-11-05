public class FiniteAutonomaException extends Exception {
    String message;

    public FiniteAutonomaException(String message) {
        this.message = message;
    }

    public FiniteAutonomaException(String message, String message1) {
        super(message);
        this.message = message1;
    }
}
