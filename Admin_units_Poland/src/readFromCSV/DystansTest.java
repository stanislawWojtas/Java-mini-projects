package readFromCSV;

public class DystansTest {
    public static void main(String[] args) {
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("src/admin-units.csv");

        AdminUnit opoczno = adminUnitList.findByName("Opoczno", 8);
        AdminUnit krakow = adminUnitList.findByName("Kraków", 6);
        System.out.println(krakow.bbox.disctanceTo(opoczno.bbox));
        System.out.println(opoczno.getWKT());
        //odległosc powinna wynosic okolo 150km. Ponadto program wydrukowal WKT dla Opoczna
    }
}
