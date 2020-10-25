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
        List<Pair<String, Integer>> initial = new ArrayList<>();
        String e = "";
        for (int j = 0; j < this.program.length(); j++) {
            if (isSeparator(this.program.charAt(j))) {
                initial.add(new Pair<>(e, line));
                initial.add(new Pair<>(";", line));
                line++;
                e = "";
            } else if (this.program.charAt(j) != ' ' && !Character.isWhitespace(this.program.charAt(j))) {
                e += this.program.charAt(j);
            }
            else if (this.program.charAt(j) == ' ' && e.length() > 0) {
                initial.add(new Pair<>(e, line));
                e = "";
            } else if (Character.isWhitespace(this.program.charAt(j)) && e.length() > 0) {
                initial.add(new Pair<>(e, line));
                line++;
                e = "";
            }
        }

        System.out.println(initial);

        int s = 0;
        while (s < initial.size()) {
            String element = initial.get(s).getElement1();
            Integer position = initial.get(s).getElement2();
            String newString = "";
            if(!addToPif(element))
            {
                List<Pair<String, Integer>> tokens = new ArrayList<>();
                for (int i = 0; i < element.length(); i++) {
                    if (newString.length() == 0) {
                        newString = String.valueOf(element.charAt(i));
                    }
                    else if (isOperator(newString) || isSeparator(newString)) {
                        tokens.add(new Pair<>(newString, position));
                        newString = "";
                    } else if (Character.isLetterOrDigit(newString.charAt(newString.length() - 1)) && Character.isLetterOrDigit(element.charAt(i)))
                        newString += String.valueOf(element.charAt(i));
                    else {
                        tokens.add(new Pair<>(newString, position));
                        newString = String.valueOf(element.charAt(i));
                    }

                }
                if (newString.length() > 0)
                    tokens.add(new Pair<>(newString, position));
                for (Pair<String, Integer> token: tokens
                     ) {
                    if (!addToPif(token.getElement1()))
                        this.error.add("error at line: " + position + ", token: " + element);
                }
            }
            s++;
        }

    }

    private boolean addToPif(String element) {
        if (isOperator(element) || isSeparator(element) || isReservedWord(element))
        {
            this.pif.add(element, new Pair(-1, -1));
            return true;
        }
        else if (isIdentifier(element) || isConstant(element)){
            Pair pos = this.symbolTable.position(element);
            this.pif.add(element, pos);
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

    public boolean isOperator(String s) {
        return operators.contains(s);
    }

    public boolean isConstant(String s){
        if(s.equals("true") || s.equals("false"))
            return true;
        return isAValidNumber(s);
    }

    public boolean isIdentifier(String s) {
        if (s.length() == 0)
            return false;
        if (!Character.isLetter(s.charAt(0)))
            return false;
        for (int i = 1; i < s.length(); i++) {
            if (!Character.isLetter(s.charAt(i)))
                return false;
        }
        return true;
    }

    public boolean isAValidNumber(String s) {
        if (s.length() <= 1 && !Character.isDigit(s.charAt(0)))
            return false;
        else if (!Character.isDigit(s.charAt(0)) && s.charAt(0) != '-' && s.charAt(0) != '+')
            return false;
        for (int i = 1; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i)))
                return false;
        }
        return true;
    }

    public void printError(){
        if(error.size() == 0)
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
