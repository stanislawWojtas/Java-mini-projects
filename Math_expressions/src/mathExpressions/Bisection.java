package mathExpressions;

public class Bisection {

    public static double findRoot(Node expression, Variable x, double lower, double upper, double tol) {

        x.setValue(lower);
        double f_lower = expression.evaluate();
        x.setValue(upper);
        double f_upper = expression.evaluate();

        //jeżeli na funkcja na granicach jest tego samego znaku to bisekcja nie działa
        if(f_lower * f_upper > 0){
            System.out.println("Funkcja musi miec inny przedzial");
            return Double.NaN;
        }

        double mid = lower;

        while((upper - lower)/2 > tol){
            mid = (lower + upper)/2;
            x.setValue(mid);
            double f_mid = expression.evaluate();
            if(f_mid == 0){
                return mid;
            }
            else if(f_lower * f_mid < 0){
                upper = mid;
                f_upper = f_mid;
            }
            else{
                lower = mid;
                f_lower = f_mid;
            }
        }
        return mid;
    }
}
