package readFromCSV;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdminUnit {
    String name;
    int adminLevel;
    double population;
    double area;
    double density;
    AdminUnit parent;
    BoundingBox bbox = new BoundingBox();
    List<AdminUnit> children = new ArrayList<>();

    protected void fixMissingValues(){
        if((population < 0 || density < 0)  && parent != null){
            //uzupełnia rekurencyjnie dane parent jeżeli ich także nie ma
            if(parent.population < 0 || parent.density < 0){
                parent.fixMissingValues();
            }
            //przypisuje takie samo density jak parent
            if(parent.density > 0){
                this.density = parent.density;
            }
            //oblicza populacje
            if(this.density > 0 && this.area > 0){
                this.population = this.density * this.area;
            }
        }
    }


    public String toString(){
        if(parent == null){
            return String.format(Locale.US, "Admin Unit: %s [adminLevel: %d, population: %f, area: %f, density: %f, %s]",
                name, adminLevel, population, area, density, bbox.toString());
        }
        return String.format(Locale.US, "Admin Unit: %s [parent: %s, adminLevel: %d, population: %f, area: %f, density: %f, %s]",
                name, parent.name, adminLevel, population, area, density, bbox.toString());
    }

    //dodatkowa metoda wypisująca tekst WKT do reprezentacji Unit na mapie
    public String getWKT(){
        if(this.bbox.isEmpty()) return "Miejscowosc nie ma granic";
        return String.format(Locale.US,"LINESTRING(%f %f,%f %f,%f %f,%f %f,%f %f)",
                this.bbox.xmin, this.bbox.ymin, this.bbox.xmin, this.bbox.ymax, this.bbox.xmax, this.bbox.ymax,
                this.bbox.xmax, this.bbox.ymin, this.bbox.xmin, this.bbox.ymin);
    }
}
