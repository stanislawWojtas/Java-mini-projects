package mathExpressions;

import java.util.ArrayList;
import java.util.List;

public class Sum extends Node {
    List<Node> args = new ArrayList<>();

    Sum() {}

    Sum(Node n1, Node n2) {
        add(n1);
        add(n2);
    }

    Sum add(Node n) {
        args.add(n);
        return this; // Zwracamy obiekt Sum
    }

    Sum add(double c) {
        return add(new Constant(c)); // Dodajemy stałą
    }

    Sum add(double c, Node n) {
        Node mul = new Prod(c, n);
        return add(mul); // Dodajemy iloczyn do sumy
    }

    @Override
    Node diff(Variable var) {
        Sum r = new Sum();
        for (Node n : args) {
            Node d = n.diff(var);

            r.add(d);// Obliczamy pochodne składników
        }
        return r;
    }

    @Override
    double evaluate() {
        if (args.isEmpty()) {
            return 0; // Jeśli nie ma argumentów, wynik to zero
        }
        double result = 0;
        for (Node arg : args) {
            result += arg.evaluate(); // Sumujemy wartości
        }
        return sign * result; // Zwracamy wynik z uwzględnieniem znaku
    }

    @Override
    public String toString() {
        if (args.isEmpty()) return "0"; // Jeśli nie ma argumentów, zwracamy "0"
        StringBuilder b = new StringBuilder();
        if (sign < 0) b.append("-(");
        boolean first = true; //flaga która wskazuje czy dodaliśmy pierszy element
        for (int i = 0; i < args.size(); i++) {
            Node arg = args.get(i);
            if(arg.isZero()){
                continue;
            }
            if (!first) {
                b.append(" + ");
            }
            b.append(arg.toString());
            first = false; //juz nie jest pierwszym elementem
        }
        //jeżeli wszystkie elementy były zerowe to zwracamy 0
        if(first) return "0";
        if (sign < 0) b.append(")");
        return b.toString();
    }

    @Override
    boolean isZero() {
        if (args.isEmpty()) return true; // Pusta suma to zero
        for (Node arg : args) {
            if (!arg.isZero()) {
                return false; // Jeśli jakiekolwiek wyrażenie nie jest zerowe, suma nie jest zerowa
            }
        }
        return true; // Wszystkie argumenty są zerowe
    }

}
