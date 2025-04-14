package mathExpressions;

public class Variable extends Node {
    String name;
    Double value;
    Variable(String name){
        this.name = name;
    }
    void setValue(double d){
        value = d;
    }

    @Override
    Node diff(Variable var){
        if(var.name.equals(name)){
            return new Constant(sign);
        }
        else{
            return new Constant(0);
        }
    }

    @Override
    boolean isZero(){
        return value != null && value == 0.0;
    }

    @Override
    double evaluate() {
         return sign*value;
    }


    @Override
    public String toString() {
        String sgn=sign<0?"-":"";
        return sgn+name;
    }

}