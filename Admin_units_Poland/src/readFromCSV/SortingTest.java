package readFromCSV;

import java.util.Comparator;

public class SortingTest {
    public static void main(String[] args) {
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("src/admin-units.csv");
        adminUnitList.sortInplaceByPopulation();

        //wypisuje 100 elemetow z limit
        adminUnitList.list(System.out, 0, 100);

    }
}
