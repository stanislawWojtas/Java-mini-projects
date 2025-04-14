package readFromCSV;

import java.io.PrintStream;
import java.util.*;
import java.util.function.Predicate;

public class AdminUnitList {
    List<AdminUnit> units = new ArrayList<>();
    Map<Long, AdminUnit> idToUnit = new HashMap<>();
    Map<AdminUnit, Long> parents = new HashMap<>();
    Map<Long, List<AdminUnit>> parentIdToChild = new HashMap<>();

    public void read(String filename){
        CSVReader reader = new CSVReader(filename);
        while(reader.next()){
            AdminUnit unit = new AdminUnit();

            //każdy z atrybutów będziemy pobierać w ten sposób
            //if(reader.isMissing("name")){
            //  unit.name = "";   w przypadku wartości liczbowych będzie to -1
            //}
            //else{
            //  unit.name = reader.get("name");
            //}
            unit.name = reader.isMissing("name") ? "" : reader.get("name");
            unit.adminLevel = reader.isMissing("admin_level") ? -1 : reader.getInt("admin_level");
            unit.area = reader.isMissing("area") ? -1 : reader.getDouble("area");
            unit.population = reader.isMissing("population") ? -1 : reader.getDouble("population");
            unit.density = reader.isMissing("density") ? -1 : reader.getDouble("density");

            //uzupełnienie współrzędnych BoundingBox
            //wystarczy dodać dwa punkty za pomocą addPoin()
            //jeżeli punktu nie ma to jest wpisywane Double.NaN
            double x1 = reader.isMissing("x1") ? Double.NaN : reader.getDouble("x1");
            double x2 = reader.isMissing("x3") ? Double.NaN : reader.getDouble("x3");
            double y1 = reader.isMissing("y1") ? Double.NaN : reader.getDouble("y1");
            double y2 = reader.isMissing("y3") ? Double.NaN : reader.getDouble("y3");
            unit.bbox.addPoint(x1, y1);
            unit.bbox.addPoint(x2, y2);

            //na początku każdy parent = null (potem się to zmieni)
            unit.parent = null;

            //dodanie unit do listy
            units.add(unit);

            //pobranie id (nie powinno być puste więc nie trzeba isMissing()) i parentId
            Long unitId = reader.getLong("id");
            Long parentId = reader.isMissing("parent") ? null : reader.getLong("parent");

            //wypełnienie mapy id -> AdminUnit
            idToUnit.put(unitId, unit);
            //wypełnienie mapy unit -> parentId
            parents.put(unit, parentId);

            //uwtorzenie dla kazdego unit mapy z dziećmi (na razie pusta)
            parentIdToChild.put(unitId, new ArrayList<>());
        }

        //wypełnienie parent w każdym unit
        for(AdminUnit unit : units){
            Long parentId = parents.get(unit);
            if(parentId != null){
                AdminUnit parentUnit = idToUnit.get(parentId);
                unit.parent = parentUnit;
                //dodatkowo wypełnienie listy dzieci
                if(parentUnit != null){
                    parentUnit.children.add(unit);
                }
            }
        }

        //Wywołanie fixMissingValues()
        fixMissingValues();
    }

    void list(PrintStream out){
        for(AdminUnit unit : units){
            out.println(unit.toString());
        }
    }

    void list(PrintStream out, int offset, int limit){
        for(int idx = offset; idx < offset + limit && idx < units.size(); idx++){
            out.println(units.get(idx).toString());
        }
    }

    AdminUnitList selectByName(String pattern, boolean regex){
        AdminUnitList ret = new AdminUnitList();
        if(regex){
            for(AdminUnit unit : units){
                if(unit.name.matches(pattern)){
                    ret.units.add(unit);
                }
            }
        }else{
            for(AdminUnit unit : units){
                if(unit.name.contains(pattern)){
                    ret.units.add(unit);
                }
            }
        }

        return ret;
    }

    private void fixMissingValues(){
        for(AdminUnit unit : units){
            unit.fixMissingValues();
        }
    }

    //funkcja sprawdzajaca sasiadow danego unit
    AdminUnitList getNeighbors(AdminUnit unit, double maxdistance){
        AdminUnitList neighbors = new AdminUnitList();
        for(AdminUnit candidate : units){
            //jezeli kandydat ma inny adminLevel lub jest samym soba to pomijamy
            if(candidate.adminLevel != unit.adminLevel || candidate.equals(unit)){
                continue;
            }
            //jezeli jest to miejscowosc to sprawdzamy odleglosc miedzy nimi
            if(candidate.adminLevel == 8){
                double distance = candidate.bbox.disctanceTo(unit.bbox);
                if(distance <= maxdistance){
                    neighbors.units.add(candidate);
                }
            } //jezeli nie jest to miejscowosc
            else{
                if(candidate.bbox.intesects(unit.bbox)){
                    neighbors.units.add(candidate);
                }
            }
        }
        return neighbors;
    }

    //Pomocnicza funkcja do znajdowania za pomoca nazwy
    AdminUnit findByName(String name, int adminLevel){
        for(AdminUnit unit : units){
            if(unit.name.equals(name) && unit.adminLevel == adminLevel){
                return unit;
            }
        }
        throw new RuntimeException("Nie znaleziono miejscowosci: " + name+ "o adminLevel = " + adminLevel);
    }

    //sortowanie po nazwie uzywajac lokalnej klasy wewnetrznej
    AdminUnitList sortInplaceByName(){
        class NameComparator implements Comparator<AdminUnit> {
            @Override
            public int compare(AdminUnit x, AdminUnit y){
                return x.name.compareTo(y.name);
            }
        }
        units.sort(new NameComparator());
        return this;
    }

    //sortowanie po powierzchni uzywajac anonimowej klasy lokalnej (od największej powierzchni do najmniejszej)
    AdminUnitList sortInplaceByArea(){
        units.sort(new Comparator<AdminUnit>() {
            @Override
            public int compare(AdminUnit x, AdminUnit y){
                return Double.compare(y.area, x.area);
            }
        });
        return this;
    }

    //sortowanie po populacji uzywajac wyrazen lambda (od najwiekszej populacji)
    AdminUnitList sortInplaceByPopulation(){
        units.sort((x, y) -> Double.compare(y.population, x.population));
        return this;
    }

    //ogólne funkcje sortowania
    AdminUnitList sortInplace(Comparator<AdminUnit> cmp){
        units.sort(cmp);
        return this;
    }

    AdminUnitList sort(Comparator<AdminUnit> cmp){
        AdminUnitList result = new AdminUnitList();
        result.units.addAll(this.units);
        result.sortInplace(cmp);
        return result;
    }

    //filtrowanie
    AdminUnitList filter(Predicate<AdminUnit> pred){
        AdminUnitList result = new AdminUnitList();
        for(AdminUnit unit : units){
            if(pred.test(unit)){
                result.units.add(unit);
            }
        }
        return result;
    }

    //funkcje filtrowania z ograniczeniami
    AdminUnitList filter(Predicate<AdminUnit> pred, int limit){
        AdminUnitList result = new AdminUnitList();
        int count = 0;
        for(AdminUnit unit : units){
            if(pred.test(unit)){
                result.units.add(unit);
                count++;
                if(count >= limit) break;
            }
        }
        return result;
    }

    AdminUnitList filter(Predicate<AdminUnit> pred, int limit, int offset){
        AdminUnitList result = new AdminUnitList();
        int count = 0;
        int added = 0;
        for(AdminUnit unit : units){
            if(pred.test(unit)){
                if(count >= offset) {
                    result.units.add(unit);
                    added++;
                    if(added >= limit) break;
                }
                count++;
            }
        }
        return result;
    }
}
