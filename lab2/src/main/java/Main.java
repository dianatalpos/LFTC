import scanner.Scanner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<String> keywords = readTokenFile();

        String p1err = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\p1err.txt"), StandardCharsets.US_ASCII);
        String p1 = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\p1.txt"), StandardCharsets.US_ASCII);
        String p2 = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\p2.txt"), StandardCharsets.US_ASCII);
        String p3 = Files.readString(Path.of("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\p3.txt"), StandardCharsets.US_ASCII);

        scan(p1err, keywords);
    }

    private static void scan(String program, List<String> keywords) throws IOException {

        Scanner scanner = new Scanner(program, keywords);
        scanner.run();
        scanner.printError();
        scanner.printPIFAndSymbolTable();
    }

    public static List<String> readTokenFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("C:\\MyApps\\faculta\\lftc\\LFTC\\lab2\\src\\main\\resources\\token.in"));

        String line = reader.readLine();
        List<String> keywords = new ArrayList<>();

        while(line!= null){
            line = line.strip();
            keywords.add(line);
            line = reader.readLine();
        }
        reader.close();

        return keywords;
    }
}
