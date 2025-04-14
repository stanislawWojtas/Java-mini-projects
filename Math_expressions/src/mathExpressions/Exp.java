package mathExpressions;

public class Exp extends Node{
    Node arg;

    Exp(Node arg){
        this.arg = arg;
    }

    //pochodna jako iloczyn funkcji w wykładniku i exp(arg)
    @Override
    Node diff(Variable var) {
        return new Prod(new Exp(arg), arg.diff(var));
    }

    //exp(x) nigdy nie jest równe 0
    @Override
    boolean isZero(){
        return false;
    }

    @Override
    double evaluate() {
        return Math.exp(arg.evaluate());
    }

    @Override
    public String toString() {
        return "exp(" + arg.toString() + ")";
    }
}
