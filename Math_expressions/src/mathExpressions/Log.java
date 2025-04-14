package mathExpressions;
//logarytm naturalny
public class Log extends Node{
    Node arg;

    Log(Node arg) {
        this.arg = arg;
    }

    //z definicji pochodnej logarytmu naturalnego 1/x * x'
    @Override
    Node diff(Variable var){
        return new Prod(new Power(arg, -1), arg.diff(var));
    }

    //logarytm naturalny nigdy nie jest rowny 0
    @Override
    boolean isZero() {
        return false;
    }

    @Override
    double evaluate(){
        return Math.log(arg.evaluate());
    }

    @Override
    public String toString() {
        return "log(" + arg.toString() + ")";
    }
}
