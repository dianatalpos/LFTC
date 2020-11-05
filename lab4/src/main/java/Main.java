import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static int menu(){
        System.out.println("1. Display set of states");
        System.out.println("2. Display alphabet");
        System.out.println("3. Display initial state");
        System.out.println("4. Display final states");
        System.out.println("5. Display transitions");
        System.out.println("6. Check sequence\n \n");


        System.out.println("0. Display transitions");


        System.out.println("Give option: ");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        return choice;
    }

    public static void run(Automata automata) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            try {
                int option = menu();

                switch (option) {
                    case 1:
                        System.out.println(automata.getStates());
                        break;
                    case 2:
                        System.out.println(automata.getAlphabet());
                        break;
                    case 3:
                        System.out.println(automata.getInitialState());
                        break;
                    case 4:
                        System.out.println(automata.getFinalStates());
                        break;
                    case 5:
                        System.out.println(automata.getTransitions());
                        break;
                    case 6:
                        checkSequence();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        throw new AssertionError("\nError - Unknown operation \n");
                }
            } catch (InputMismatchException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void checkSequence() {

    }

    public static void main(String[] args) throws IOException {
        Automata automata = new Automata();

        try {
            automata.readFA();
        } catch (FiniteAutonomaException exception) {
            System.out.println(exception.message);
            System.exit(-1);
        }

        run(automata);
    }
}
