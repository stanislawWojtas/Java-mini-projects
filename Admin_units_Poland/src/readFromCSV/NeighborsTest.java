package readFromCSV;

import java.util.Locale;

public class NeighborsTest {
    public static void main(String[] args) {
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("src/admin-units.csv");
        AdminUnit warszawa = adminUnitList.findByName("Wieliczka", 8);
        //pomiar czasu
        double t1 = System.nanoTime()/1e6;
        //wyszukuje sąsiadów Wieliczki w odległosci 15 km
        AdminUnitList neighbors = adminUnitList.getNeighbors(warszawa, 15);
        double t2 = System.nanoTime()/1e6;
        neighbors.list(System.out);
        System.out.printf(Locale.US,"t2-t1=%f\n",t2-t1);
    }
}
