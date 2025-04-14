package mathExpressions;

import java.util.ArrayList;
import java.util.List;
public class Circle {
    public static void pointsInCircle() {
        Variable x = new Variable("x");
        Variable y = new Variable("y");
        Node circle = new Sum()
                .add(new Power(x, 2))
                .add(new Power(y, 2))
                .add(8, x)
                .add(4, y)
                .add(16);

        System.out.print("f(x,y)=");
        System.out.println(circle.toString());

        List<Double> x_points = new ArrayList<>();
        List<Double> y_points = new ArrayList<>();

        while (x_points.size() < 100) {
            double val_x = Math.random() * 40 - 20;
            double val_y = Math.random() * 40 - 20;

            x.setValue(val_x);
            y.setValue(val_y);

            double val_f = circle.evaluate();

            if (val_f < 0) {
                x_points.add(val_x);
                y_points.add(val_y);
            }
        }

        for (int i = 0; i < x_points.size(); i++) {
            System.out.println("Point " + (i+1) +": (" + x_points.get(i) + ", " + y_points.get(i) + ")");
        }
    }
}
