import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static Grammar grammar;

    public static void main(String[] args) throws IOException, ConflictException {
        grammar = new Grammar();
        run();
    }

    private static void menu() {
        System.out.println("1 - Non-terminals");
        System.out.println("2 - Terminals");
        System.out.println("3 - Initial state");
        System.out.println("4 - Productions");
        System.out.println("5 - Production for a given non-terminal");
    }

    private static void run() throws IOException, ConflictException {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        parseSequence1();

        while (running) {
            menu();
            System.out.println("Give your option: ");

            try {
                int option = scanner.nextInt();

                switch (option) {
                    case 1:
                        displayNonTerminals();
                        break;
                    case 2:
                        displayTerminals();
                        break;
                    case 3:
                        displayInitialState();
                        break;
                    case 4:
                        displayProductions();
                        break;
                    case 5:
                        displayProductionsForNonTerminal();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        throw new AssertionError("\nError - Unknown operation \n");
                }
            } catch (InputMismatchException | AssertionError e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void displayNonTerminals() {
        for(String nonTerminal : grammar.getNonTerminals()) {
            System.out.print(nonTerminal + " ");
        }
        System.out.println();
    }

    private static void displayTerminals() {
        for(String terminal : grammar.getTerminals()) {
            System.out.print(terminal + " ");
        }
        System.out.println();
    }

    private static void displayInitialState() {
        System.out.println(grammar.getInitialState());
    }

    private static void displayProductions() {
        Map<String, List<Pair<Integer, List<String>>>> productions = grammar.getProductions();
        for(String nonTerminal : productions.keySet()) {
            for(Pair<Integer, List<String>> value: productions.get(nonTerminal)) {
                String valueToPrint = value.element2.stream().collect(Collectors.joining(" "));
                System.out.println(nonTerminal + " -> " + valueToPrint);
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void displayProductionsForNonTerminal() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("NonTerminal: ");
        String nonTerminal = scanner.nextLine();

        List<Pair<Integer, List<String>>> productions = grammar.getProductionsForNonTerminal(nonTerminal);
        for(Pair<Integer, List<String>> value: productions) {
            String valueToPrint = value.element2.stream().collect(Collectors.joining(" "));
            System.out.println(nonTerminal + " -> " + valueToPrint);
        }
        System.out.println();
    }

    private static void parseSequence1() throws IOException, ConflictException {

        BufferedReader reader;
        reader = new BufferedReader(new FileReader("src//main//resources//seqtest2.txt"));
        String sequence = reader.readLine();

        Parser parser = new Parser(grammar);
        parser.parse(sequence);
        parser.printTable("out1.out");
    }
}
