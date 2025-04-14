package readFromCSV;

public class AdminUnitTest {
    public static void main(String[] args){
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("src/admin-units.csv");
        //tworze liste z miejscowosciami zawierającymi "nowa"
        //Można przy okazji zobaczyc, ze density i population sa wszedzie uzupelnione
        AdminUnitList nowaList = adminUnitList.selectByName("Nowa", false);

        nowaList.list(System.out);
    }
}
