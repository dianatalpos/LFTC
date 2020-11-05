package table;

import java.io.FileWriter;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class PIF {

    private List<PIFElement> table;

    public PIF() {
        table = new ArrayList<>();
    }


    public void add(String token, Pair position){
        PIFElement pifElement = new PIFElement(token,position);
        table.add(pifElement);
    }

    public void printPIF() throws IOException {
        FileWriter pifWriter = new FileWriter("PIF.out");
        for (PIFElement pair : table) {
            pifWriter.write(pair.toString());
            pifWriter.write("\r\n");
        }
        pifWriter.close();
    }
}
