package mathExpressions;

import java.util.ArrayList;
import java.util.List;

public class Prod extends Node {
    List<Node> args = new ArrayList<>();

    Prod(){}

    Prod(Node n1){
        args.add(n1);
    }
    Prod(double c){
        this(new Constant(c));
    }

    Prod(Node n1, Node n2){
        args.add(n1);
        args.add(n2);
    }

    Prod(double c, Node n){
        this(new Constant(c), n);
    }


    //dodanie warunku z simplify
    Prod mul(Node n){
        args.add(n);
        return this.simplify();
    }

    Prod mul(double c){
        //dodaje stałą jako składnik mnożenia
        args.add(new Constant(c));
        return this.simplify();
    }

    public Prod simplify(){
        //tu będzie trzymany iloczyn wsyztskich stałych w naszym Prod
        double constProd = 1.0;
        //tworze nową listę, która będzie <= od starej
        List<Node> newArgs = new ArrayList<>();

        //jeżeli jest stała to ją nie dodajemy na razie
        for (Node arg : args){
            if(arg instanceof Constant){
                constProd *= arg.evaluate();
            }
            else{
                newArgs.add(arg);
            }
        }
        //jeżeli iloczyn stałych jest 0 to zwracamy 0;
        if(constProd == 0){
            return new Prod(0);
        }

        //jeżeli lista nie jest pusta lub iloczn stałych != 1 to dodajemy tą stałą na początek listy
        if(constProd != 1 || newArgs.isEmpty()){
            newArgs.add(0, new Constant(constProd));
        }

        args = newArgs;

        //jeżeli lista zawiera jeden element to zwracamy go zamiast całej instancji Prod
        if(args.size() == 1){
            Node singleNode = args.get(0);
            if(singleNode instanceof Prod){
                return (Prod)singleNode;
            }
        }

        return this;
    }

    @Override
    double evaluate() {
        //jezeli nie ma elementow to zwraca 1
        if(args.isEmpty()) return 1;
        double result =1;
        // oblicz iloczyn czynników wołąjąc ich metodę evaluate
        for(Node arg : args){
            result *= arg.evaluate();
        }
        return sign*result;
    }
    int getArgumentsCount(){return args.size();}

    @Override
    Node diff(Variable var){
        Sum r = new Sum();
        for(int i = 0; i < args.size(); i++){
            Prod m = new Prod();
            for(int j = 0; j < args.size(); j++){
                Node f = args.get(j);
                if(j==i)m.mul(f.diff(var));
                else m.mul(f);
            }
            r.add(m.simplify());
        }
        return r;
    }

    @Override
    boolean isZero(){
        if(args.isEmpty()) return false ;
        for (Node arg : args){
            if(arg.isZero()) return true;
        }
        return false;
    }

    @Override
    public String toString(){
        StringBuilder b =  new StringBuilder();
        if(sign<0)b.append("-");
        for(int i = 0; i < args.size(); i++){
            Node arg = args.get(i);
            //jezeli jeden ze czynników jest równy 0 to całt iloczyn jest równy 0
            if(arg.isZero()) {
                return "0";
            }
            if(arg.getSign() < 0){
                b.append("(");
            }
            b.append(arg.toString());
            if(arg.getSign() < 0){
                b.append(")");
            }
            //jeśli nie jest to ostatni znak to dodajemy znak mnożenia
            if(args.size() - 1 > i){
                b.append("*");
            }
        }
        return b.toString();
    }


}