import scanner.Scanner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
        String program = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\p3.txt"), StandardCharsets.US_ASCII);
        String tokens = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\token.in"), StandardCharsets.US_ASCII);


        StringTokenizer tokensFromFile = new StringTokenizer(tokens, " ");
        List<String> keywords = new ArrayList<>();
        while (tokensFromFile.hasMoreTokens())
            keywords.add(tokensFromFile.nextToken());

        Scanner scanner = new Scanner(program, keywords);
        scanner.run();
        scanner.printError();
        scanner.printPIFAndSymbolTable();
    }
}
