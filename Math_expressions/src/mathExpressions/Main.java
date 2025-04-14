package mathExpressions;

import java.sql.SQLOutput;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Variable x = new Variable("x");
        Node exp = new Sum()
                .add(2.1,new Power(x,3))
                .add(new Power(x,2))
                .add(-2,x)
                .add(7);
        System.out.println("Wynik 1:    " + exp.toString());
        //Oczekiwany wynik: 2.1*x^3 + x^2 + (-2)*x + 7

        System.out.println();

        Variable x2 = new Variable("x");
        Node exp2 = new Sum()
                .add(2,new Power(x2,3))
                .add(new Power(x2,2))
                .add(-2,x2)
                .add(7);
        System.out.print("exp=");
        System.out.println(exp2.toString());

        Node d = exp2.diff(x2);
        System.out.print("d(exp)/dx=");
        System.out.println(d.toString());
        //oczekiwany wynik:
        //exp=2*x^3 + x^2 + (-2)*x + 7
        //d(exp)/dx=6*x^2 + 2*x^1 + (-2)
        //zgadza się wszytsko oprócz pierwszego wyrazu w pochodnej gdzie program nie działa gdy wyrażenie jest pochodną
        System.out.println();

        Variable x3 = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x3,2))
                .add(new Power(y,2))
                .add(8,x3)
                .add(4,y)
                .add(16);
        System.out.print("f(x,y)=");
        System.out.println(circle.toString());

        Node dx = circle.diff(x3);
        System.out.print("d f(x,y)/dx=");
        System.out.println(dx.toString());
        System.out.print("d f(x,y)/dy=");
        Node dy = circle.diff(y);
        System.out.println(dy.toString());
        //oczekiwane wyniki
        //f(x,y)=x^2 + y^2 + 8*x + 4*y + 16
        //d f(x,y)/dx=2*x^1 + 8
        //d f(x,y)/dy=2*y^1 + 4
        //zgadzają się


        //znajdowanie 100 punktow w kole
        Circle.pointsInCircle();
    }
}