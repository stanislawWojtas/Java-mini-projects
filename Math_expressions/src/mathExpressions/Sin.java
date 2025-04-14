package mathExpressions;

public class Sin extends Node{
    Node arg;

    Sin(Node arg) {
        this.arg = arg;
    }

    //pochodna jako iloczyn pochodnej wewnątrz sinusa i cos tego samego argumentu
    @Override
    Node diff(Variable var){
        return new Prod(new Cos(arg), arg.diff(var));
    }

    @Override
    boolean isZero() {
        return Math.sin(arg.evaluate()) == 0;
    }

    @Override
    double evaluate(){
        return Math.sin(arg.evaluate());
    }

    @Override
    public String toString() {
        return "sin(" + arg.toString() + ")";
    }
}
