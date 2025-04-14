public class Main {
    public static void main(String[] args) {
        Matrix m = new Matrix(new double[][]{
                {1,2},
                {3,4}
        });
        System.out.println(m.determinant());
    }
}
