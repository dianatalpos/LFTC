import java.util.Objects;

public class Pair<A,B> {
    A element1;
    B element2;

    public Pair(A element1, B element2) {
        this.element1 = element1;
        this.element2 = element2;
    }

    public Pair() {
    }

    public A getElement1() {
        return element1;
    }

    public B getElement2() {
        return element2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(element1, pair.element1) &&
                Objects.equals(element2, pair.element2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element1, element2);
    }

    @Override
    public String toString() {
        return  "[" + element1 +
                " : " + element2 +
                ']';
    }
}
