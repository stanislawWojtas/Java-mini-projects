package readFromCSV;

public class QueryTest {
    public static void main(String[] args) {
        AdminUnitList adminUnitList = new AdminUnitList();
        adminUnitList.read("src/admin-units.csv");

        AdminUnitQuery query1 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.area>1000)
                .or(a->a.name.startsWith("Sz"))
                .sort((a,b)->Double.compare(a.area,b.area))
                .limit(100);


        //zapytanie zwracające powiaty w województwie małopolskim posortowane alfabetycznie
        AdminUnitQuery query2 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.adminLevel == 6 && a.parent != null && a.parent.name.equals("województwo małopolskie"))
                .sort((a,b)->a.name.compareTo(b.name));


        //jednostki nie wojewódzkie mające populacje > 500 000 posortowane po populacji
        AdminUnitQuery query3 = new AdminUnitQuery()
                .selectFrom(adminUnitList)
                .where(a->a.population > 500000)
                .and(a->a.adminLevel > 4)
                .sort((a,b)->Double.compare(a.population,b.population));


        //jednostki o nazwie zawierającej "Nowa", sortowane po populacji
        AdminUnitQuery query4 = new AdminUnitQuery()
            .selectFrom(adminUnitList)
            .where(a -> a.name.contains("Nowa"))
            .sort((a, b) -> Double.compare(b.population, a.population));


        //odkomentować poszczególne zapytania
        query1.execute().list(System.out);
        //query2.execute().list(System.out);
        //query3.execute().list(System.out);
        //query4.execute().list(System.out);

        //sprawdzenie filter (odkomentować)
        //wypisuje miejscowosci na 'Ż' posortowane po powierzchni
        //adminUnitList.filter(a->a.name.startsWith("Ż")).sortInplaceByArea().list(System.out);
    }
}
