import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.stream.Collectors;

public class Derivation {
    private Stack<String> alpha;
    private Stack<String> beta;
    private List<Integer> pi;

    public Derivation(Stack<String> alpha, Stack<String> beta, List<Integer> pi) {
        this.alpha = (Stack<String>) alpha.clone();
        this.beta = (Stack<String>) beta.clone();
        this.pi = new ArrayList<>();
        this.pi.addAll(pi);
    }

    public Derivation() {
        alpha = new Stack();
        beta= new Stack();
        pi = new ArrayList();
    }

    public Stack getAlpha() {
        return alpha;
    }

    public void setAlpha(Stack alpha) {
        this.alpha = alpha;
    }

    public Stack getBeta() {
        return beta;
    }

    public void setBeta(Stack beta) {
        this.beta = beta;
    }

    public List getPi() {
        return pi;
    }

    public void setPi(List pi) {
        this.pi = pi;
    }

    public boolean isNotEmpty() {
        return !(alpha.isEmpty() && beta.isEmpty() && pi.isEmpty());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Derivation that = (Derivation) o;
        return Objects.equals(alpha, that.alpha) &&
                Objects.equals(beta, that.beta) &&
                Objects.equals(pi, that.pi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alpha, beta, pi);
    }

    @Override
    public String toString() {
        Stack<String> alpha2 = (Stack<String>) alpha.clone();
        String result =  "( ";
        while(!alpha2.isEmpty()){
            String pop = alpha2.pop();
            result += pop;
        }

        result+=" , ";

        Stack<String> beta2 = (Stack<String>) beta.clone();
        while(!beta2.isEmpty()){
            String betaPop = beta2.pop();
            result += betaPop;
        }

        result+=" , ";

        String piString = pi.stream().map(integer-> String.valueOf(integer))
                .collect(Collectors.joining(""));

        result += piString;
        result += " )";

        return result;
    }
}
