import java.util.Random;
public class Matrix {
    double[]data;
    int rows;
    int cols;

    Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        data = new double[rows*cols];
    }

    Matrix(double[][] d){
        rows = d.length;
        cols = d[0].length;
        for(int i = 1; i < rows; i++){
            cols = Math.max(cols, d[i].length);
        }
        data = new double[rows * cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < d[i].length; j++){
                data[i*cols + j] = d[i][j];
            }
        }
    }

    double[][] asArray(){
        double[][] array = new double[rows][cols];
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                array[i][j] = data[i*cols + j];
            }
        }
        return array;
    }

    double get(int r, int c){
        return data[r*cols + c];
    }

    void set(int r, int c, double value){
        data[r*cols + c] = value;
    }

    public String toString(){
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        for(int i=0;i<rows;i++){
            buf.append("[");
            for(int j = 0; j < cols; j++){
                buf.append(this.get(i, j)).append(", ");
            }
            buf.append("]\n");
        }
        if(rows > 0) { //jezeli bedzie pusta macierz to metoda powinna zwrocic "[]"
            buf.deleteCharAt(buf.length() - 1);
        }
        buf.append("]");
        return buf.toString();
    }

    void reshape(int newRows,int newCols){
        if(rows*cols != newRows*newCols)
            throw new RuntimeException(String.format("%d x %d matrix can't be reshaped to %d x %d",rows,cols,newRows,newCols));
        this.rows = newRows;
        this.cols = newCols;
    }

    int [] shape(){
        return new int[]{rows,cols};
    }

    public Matrix add(Matrix m){
        if(this.rows != m.rows || this.cols != m.cols){
            throw new RuntimeException("Matrix shape mismatch");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) + m.get(i, j));
            }
        }
        return result;
    }

    public Matrix sub(Matrix m){
        if(this.rows != m.rows || this.cols != m.cols){
            throw new RuntimeException("Matrix shape mismatch");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) - m.get(i, j));
            }
        }
        return result;
    }

    public Matrix mul(Matrix m){
        if(this.rows != m.rows || this.cols != m.cols){
            throw new RuntimeException("Matrix shape mismatch");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) * m.get(i, j));
            }
        }
        return result;
    }

    public Matrix div(Matrix m){
        if(this.rows != m.rows || this.cols != m.cols){
            throw new RuntimeException("Matrix shape mismatch");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                if(m.get(i, j) == 0){
                    throw new RuntimeException("Cannot divide by zero");
                }
                result.set(i, j, this.get(i, j) / m.get(i, j));
            }
        }
        return result;
    }

    public Matrix add(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) + w);
            }
        }
        return result;
    }

    public Matrix sub(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) - w);
            }
        }
        return result;
    }

    public Matrix mul(double w){
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) * w);
            }
        }
        return result;
    }

    public Matrix div(double w){
        if(w == 0){
            throw new RuntimeException("Cannot divide by zero");
        }
        Matrix result = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                result.set(i, j, this.get(i, j) / w);
            }
        }
        return result;
    }

    public Matrix dot(Matrix m){
        if(this.rows != m.cols){
            throw new RuntimeException("Matrix shape mismatch");
        }
        Matrix result = new Matrix(this.rows, m.cols);
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < m.cols; j++){
                double sum = 0;
                for(int k = 0; k < this.cols; k++){
                    sum += this.get(i, k) * m.get(k, j);
                }
                result.set(i, j, sum);
            }
        }
        return result;
    }

    double frobenius(){
        double sum = 0;
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                sum += Math.pow(this.get(i, j), 2);
            }
        }
        return Math.sqrt(sum);
    }

    public static Matrix random(int rows, int cols){
        Matrix m = new Matrix(rows,cols);
        Random r = new Random();
        for(int i = 0; i < m.rows; i++){
            for(int j = 0; j < m.cols; j++){
                m.set(i, j, r.nextDouble());
            }
        }
        return m;
    }

    public static Matrix eye(int n){
        Matrix m = new Matrix(n,n);
            for(int i = 0; i < n; i++){
                m.set(i, i, 1);
            }
        return m;
    }

    void swapRows(int row1, int row2){
        for(int i = 0; i < this.cols; i++){
            double temp = this.get(row1, i);
            this.set(row1, i, this.get(row2, i));
            this.set(row2, i, temp);
        }
    }

    double determinant(){ //metoda oblicza wyznacznik macierzy za pomoca eliminacji Gaussa
        if(this.rows != this.cols){
            throw new RuntimeException("It must be a square matrix");
        }
        int n = this.rows;
        Matrix m = new Matrix(n, n); //tworze kopie macierzy zeby nie modyfikowac oryginalnej
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                m.set(i, j, this.get(i, j));
            }
        }
        double detSign = 1.0; //zmienna, ktora informuje nas o znaku wyznacznika(przy zamianie wierszy sie zmienia)
        for(int i = 0; i < n; i++){
            //szukam nawiekszego elementu w kolumnie od elementu na przekatnej i ponizej
            int maxRow = i; //wiersz tego elementu
            double maxValinCol = Math.abs(m.get(i, i));
            for(int j = i+1; j < n; j++) {
                if (Math.abs(m.get(j, i)) > maxValinCol) {
                    maxValinCol = Math.abs(m.get(j, i));
                    maxRow = j;
                }
            }
            if(maxValinCol == 0){ //mamy 0 na przekatnej wiec wyznacznik det = 0
                return 0;
            }
            if(maxRow != i){
                m.swapRows(i, maxRow);
                detSign *= -1; // zmieniamy znak wyznacznika przy zmianie kolumn
            }
            for(int j = i+1; j < n; j++){
                double factor = m.get(j, i) / m.get(i, i);
                //Odejmuje wiersze od siebie tak aby elementy ponizej przekatnej sie wyzerowaly
                for(int k = i; k < n; k++){
                    m.set(j, k, m.get(j, k) - factor * m.get(i, k));
                }
            }
        }
        double det = detSign; //uwzgledniam znak wyznacznika
        for(int i = 0; i < n; i++){ //mnozenie elementow na przekatnej
            det *= m.get(i, i);
        }
        return det;
    }
}
