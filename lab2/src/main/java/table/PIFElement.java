package table;

import java.util.AbstractMap;

public class PIFElement {
    String token;
    Pair position;


    public PIFElement(String token, Pair position) {
        this.token = token;
        this.position = position;
    }

    @Override
    public String toString() {
        return "PIFElement{" +
                "token='" + token + '\'' +
                ", position=" + position +
                '}';
    }
}
