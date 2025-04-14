package mathExpressions;

public class Cos extends Node{
    Node arg;

    Cos(Node arg) {
        this.arg = arg;
    }

    //pochodna jako iloczyn pochodnej wewnątrz cos i -sin tego samego argumentu
    @Override
    Node diff(Variable var){
        return new Prod(new Sin(arg).minus(), arg.diff(var));
    }

    @Override
    boolean isZero() {
        return Math.cos(arg.evaluate()) == 0.0;
    }

    @Override
    double evaluate(){
        return Math.cos(arg.evaluate());
    }

    @Override
    public String toString() {
        return "cos(" + arg.toString() + ")";
    }
}
