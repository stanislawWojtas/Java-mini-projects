import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatrixTest {

    @Test
    void testConstructors(){
        //konstruktor z wymiarami rows i cols
        Matrix m = new Matrix(2,3);
        assertEquals(2, m.shape()[0]);
        assertEquals(3, m.shape()[1]);
        for(int i=0; i<2; i++) {
            for(int j=0; j<3; j++) {
                assertEquals(0.0, m.get(i, j), 1e-6);
            }
        }

        //konstruktor z tablica double[][]
        Matrix m2 = new Matrix(new double[][]{{1,2,3},{4,5,6}});
        assertEquals(2, m2.shape()[0]);
        assertEquals(3, m2.shape()[1]);
        assertEquals(1, m2.get(0, 0), 1e-6);
        assertEquals(6, m2.get(1, 2), 1e-6);

    }

    @Test
    void asArray() {
        double[][] data = new double[][]{
                {1,2,3},
                {5,6,7},
                {9,10,11},
                {10,1.1,-5.2}
        };
        Matrix m = new Matrix(data);
        double[][] array = m.asArray();
        //sprawdzenie czy wszystkie wiersze sa takie same
        assertArrayEquals(data[0], array[0], 1e-6);
        assertArrayEquals(data[1], array[1], 1e-6);
        assertArrayEquals(data[2], array[2], 1e-6);
        assertArrayEquals(data[3], array[3], 1e-6);
    }

    @Test
    void get() {
        Matrix m = new Matrix(new double[][]{{12, 0, 3}, {-4, 5.2, 6}});
        assertEquals(12, m.get(0, 0), 1e-6);
        assertEquals(6, m.get(1,2), 1e-6);
    }

    @Test
    void set() {
        Matrix m = new Matrix(5,2);
        m.set(4,1, 17);
        m.set(0,0, 11);
        assertEquals(17, m.get(4,1), 1e-6);
        assertEquals(11, m.get(0,0), 1e-6);
    }

    @Test
    void testToString() {
        String s= "[[1.0,2.3,4.56], [12.3,  45, 21.8]]";
        s= s.replaceAll("(\\[|\\]|\\s)+","");
        String[] t = s.split("(,)+");
        for(String x:t){
            System.out.println(String.format("\'%s\'",x ));
        }

        double[]d=new double[t.length];
        for(int i=0;i<t.length;i++) {
            d[i] = Double.parseDouble(t[i]);
        }

        double arr[][]=new double[1][];
        arr[0]=d;

        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr[i].length;j++){
                System.out.println(arr[i][j]);
            }
        }
    }

    @Test
    void reshape() {
        Matrix m = new Matrix(4,3);
        m.reshape(2,6);
        int[] shape = m.shape();
        assertEquals(2, shape[0]);
        assertEquals(6, shape[1]);
        //przechwytywanie wyjatku, gdy wymiary sie nie zgadzaja
        assertThrows(RuntimeException.class,()->m.reshape(3,2));
    }

    @Test
    void shape() {
        Matrix m = new Matrix(7,3);
        int[] shape = m.shape();
        assertEquals(7,shape[0]);
        assertEquals(3,shape[1]);
    }

    @Test
    void add() {
        Matrix a = new Matrix(new double[][]{{8, 5, 3}, {4, 5.4, 6}});
        Matrix b = new Matrix(new double[][]{{6, 3, 2}, {7, 8.3, 1}});
        Matrix result = a.add(b);
        Matrix expected = new Matrix(new double[][]{{14,8,5},{11,13.7,7}});
        for(int i = 0; i < a.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        //dodawanie macierzy o innych wymiarach
        Matrix c = new Matrix(4,5);
        assertThrows(RuntimeException.class,()->c.add(a));
    }

    @Test
    void sub() {
        Matrix a = new Matrix(new double[][]{{8, 5, 3}, {4, 5, 6.3}});
        Matrix b = new Matrix(new double[][]{{6, 3, 2}, {7, 8, 1.2}});
        Matrix expected = new Matrix(new double[][]{{2,2,1},{-3,-3,5.1}});
        Matrix result = a.sub(b);
        for(int i = 0; i < a.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        //odejmowanie macierzy o innych wymiarach
        Matrix c = new Matrix(4,5);
        assertThrows(RuntimeException.class,()->c.sub(a));
    }

    @Test
    void mul() {
        Matrix a = new Matrix(new double[][]{{2, 10, 2.5}, {-0.2, 3, 0.1}});
        Matrix b = new Matrix(new double[][]{{2.5, -5, 4}, {-5, -4, 12}});
        Matrix expected = new Matrix(new double[][]{{5,-50,10},{1,-12,1.2}});
        Matrix result = a.mul(b);
        for(int i = 0; i < a.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        //mnozenie macierzy o innych wymiarach
        Matrix c = new Matrix(4,5);
        assertThrows(RuntimeException.class,()->c.mul(a));
    }

    @Test
    void div() {
        Matrix a = new Matrix(new double[][]{{5, 10, 12}, {33, 32, 4.5}});
        Matrix b = new Matrix(new double[][]{{2.5, 5, 4}, {11, 8, 0.5}});
        Matrix expected = new Matrix(new double[][]{{2,2,3},{3,4,9}});
        Matrix result = a.div(b);
        for(int i = 0; i < a.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        b.set(0, 0, 0); //dzielenie przez 0
        assertThrows(RuntimeException.class, () -> a.div(b));
    }

    @Test
    void testAdd() {
        Matrix m = new Matrix(new double[][]{{4, 10, 100}, {7, -2, 1.5}});
        Matrix expected = new Matrix(new double[][]{{9,15,105},{12,3,6.5}});
        Matrix result = m.add(5);
        for(int i = 0; i < m.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
    }

    @Test
    void testSub() {
        Matrix m = new Matrix(new double[][]{{4, 5.2, 100}, {10, -5, 1}});
        Matrix result = m.sub(2);
        Matrix expected = new Matrix(new double[][]{{2,3.2,98},{8,-7,-1}});
        for(int i = 0; i < m.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
    }

    @Test
    void testMul() {
        Matrix m = new Matrix(new double[][]{{4, 0.5, 100}, {2, -3, 1.5}});
        Matrix result = m.mul(3);
        Matrix expected = new Matrix(new double[][]{{12,1.5,300},{6,-9,4.5}});
        for(int i = 0; i < m.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
    }

    @Test
    void testDiv() {
        Matrix m = new Matrix(new double[][]{{12,1,-1},{30,100,2.4}});
        Matrix result = m.div(2);
        Matrix expected = new Matrix(new double[][]{{6,0.5,-0.5},{15,50,1.2}});
        for(int i = 0; i < m.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        //dzielenie przez 0
        assertThrows(RuntimeException.class, () -> m.div(0));
    }

    @Test
    void dot() {
        Matrix a = new Matrix(new double[][]{{5,2,3,8},{12,0.5,9,1},{5,6,1,8}});
        Matrix b = new Matrix(new double[][]{{0,17,2},{1.2,-4,5},{7,3,8},{11,2,0.7}});
        Matrix result = a.dot(b);
        Matrix expected = new Matrix(new double[][]{{111.4,102,49.6},{74.6,231,99.2},{102.2,80,53.6}});
        for(int i = 0; i < expected.shape()[0]; i++){
            assertArrayEquals(expected.asArray()[i], result.asArray()[i], 1e-6);
        }
        //sprawdzenie czy przy nieprawidlowych danych wyskoczy exception
        assertThrows(RuntimeException.class, () -> a.dot(a));
    }

    @Test
    void frobenius() {
        Matrix m = new Matrix(new double[][]{{1,2,3},{4,5,6}});
        //wartosc z kalkulatora
        assertEquals(9.5393920, m.frobenius(), 1e-6);
    }

    @Test
    void random() {
        //sprawdzam czy random dobrze generuje liczby z przedzialu [0,1)
        Matrix m = Matrix.random(3,2);
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 2; j++){
                double val = m.get(i,j);
                assertTrue(val >= 0.0 && val < 1.0);
            }
        }
    }

    @Test
    void eye() {
        Matrix m = Matrix.eye(16);
        assertEquals(4, m.frobenius(), 1e-6);
    }

    @Test
    void determinant() {
        //wartosc wyliczona z kalkulatora
        Matrix b = new Matrix(new double[][]{{1,-2,3},{4.5,5,6},{7,11,9}});
        assertEquals(19.5, b.determinant(), 1e-6);
    }
}