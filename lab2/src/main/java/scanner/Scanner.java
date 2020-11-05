package scanner;

import table.PIF;
import table.Pair;
import table.SymbolTable;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Scanner {
    private static final String CODE_FOR_IDENTIFIER = "0";
    private static final String CODE_FOR_CONSTANTS = "1";
    private final List<String> operators = Arrays.asList("+", "-", "*", "/", "=",
            "<", "<=", ">", ">=", "->", "<-", "!=", "[]", "[", "]");
    private final List<Character> separators = Arrays.asList(',', ';','{', '}', '(', ')');
    private PIF pif;
    private SymbolTable symbolTable;
    private List<String> keywords;
    private List<String> error;
    private String program;


    public Scanner(String program, List<String> keywords) {
        this.program = program;
        this.pif = new PIF();
        this.symbolTable = new SymbolTable();
        this.keywords = keywords;
        this.error = new ArrayList<>();
    }

    public void parse() {
        int line = 0;
        List<Pair<String, Integer>> initialArrayWithTokens = new ArrayList<>();
        String e = "";
        for (int index = 0; index < this.program.length(); index++) {
            if (isSeparator(this.program.charAt(index))) {
                initialArrayWithTokens.add(new Pair<>(e, line));
                initialArrayWithTokens.add(new Pair<>(";", line));
                line++;
                e = "";
            } else if (this.program.charAt(index) != ' ' && !Character.isWhitespace(this.program.charAt(index))) {
                e += this.program.charAt(index);
            }
            else if (this.program.charAt(index) == ' ' && !e.isEmpty()) {
                initialArrayWithTokens.add(new Pair<>(e, line));
                e = "";
            } else if (Character.isWhitespace(this.program.charAt(index)) && !e.isEmpty()) {
                initialArrayWithTokens.add(new Pair<>(e, line));
                line++;
                e = "";
            }
        }

        System.out.println(initialArrayWithTokens);

        int indexOfToken = 0;
        while (indexOfToken < initialArrayWithTokens.size()) {
            String element = initialArrayWithTokens.get(indexOfToken).getElement1();
            Integer position = initialArrayWithTokens.get(indexOfToken).getElement2();
            String newString = "";
            if(!addToPif(element))
            {
                List<Pair<String, Integer>> tokens = new ArrayList<>();
                for (int i = 0; i < element.length(); i++) {
                    if (newString.isEmpty()) {
                        newString = String.valueOf(element.charAt(i));
                    }
                    else if (isOperator(newString) || isSeparator(newString)) {
                        tokens.add(new Pair<>(newString, position));
                        newString = String.valueOf(element.charAt(i));
                    } else if (Character.isLetterOrDigit(newString.charAt(newString.length() - 1)) && Character.isLetterOrDigit(element.charAt(i)))
                        newString += String.valueOf(element.charAt(i));
                    else {
                        tokens.add(new Pair<>(newString, position));
                        newString = String.valueOf(element.charAt(i));
                    }
                }
                if (!newString.isEmpty())
                    tokens.add(new Pair<>(newString, position));
                for (Pair<String, Integer> token: tokens
                     ) {
                    if (!addToPif(token.getElement1()))
                        this.error.add("error at line: " + position + ", token: " + element);
                }
            }
            indexOfToken++;
        }

    }

    private boolean addToPif(String element) {
        if (isOperator(element) || isSeparator(element) || isReservedWord(element)) {
            this.pif.add(element, new Pair(-1, -1));
            return true;
        } else if (isIdentifier(element) ){
            Pair pos = this.symbolTable.position(element);
            this.pif.add(CODE_FOR_IDENTIFIER, pos);
            return true;
        } else if(isConstant(element)){
            Pair pos = this.symbolTable.position(element);
            this.pif.add(CODE_FOR_CONSTANTS, pos);
            return true;
        }

        return false;
    }

    private boolean isReservedWord(String element) {
        return keywords.contains(element);
    }

    private boolean isSeparator(char charAt) {
        return separators.contains(charAt);
    }

    private boolean isSeparator(String string) {
        if(string.length() > 1)
            return false;
        return separators.contains(string.charAt(0));
    }

    public void run() {
        this.parse();
    }

    public boolean isOperator(String token) {
        return operators.contains(token);
    }

    public boolean isConstant(String token){
        if(token.equals("true") || token.equals("false"))
            return true;
        return isAValidNumber(token);
    }

    public boolean isIdentifier(String token) {
        if (token.isEmpty())
            return false;
        if (!Character.isLetter(token.charAt(0)))
            return false;
        for (int i = 1; i < token.length(); i++) {
            if (!Character.isLetter(token.charAt(i)))
                return false;
        }
        return true;
    }

    public boolean isAValidNumber(String token) {
        if (token.length() <= 1 && !Character.isDigit(token.charAt(0)))
            return false;
        else if (!Character.isDigit(token.charAt(0)) && token.charAt(0) != '-' && token.charAt(0) != '+')
            return false;
        for (int i = 1; i < token.length(); i++) {
            if (!Character.isDigit(token.charAt(i)))
                return false;
        }
        return true;
    }

    public void printError(){
        if(error.isEmpty())
            System.out.println("Lexically correct!");
        else{
            error.forEach(System.out::println);
        }
    }

    public void printPIFAndSymbolTable() throws IOException {
        pif.printPIF();

        FileWriter symbolTable = new FileWriter("ST.out");
        symbolTable.write(this.symbolTable.toString());
        symbolTable.close();
    }
}
