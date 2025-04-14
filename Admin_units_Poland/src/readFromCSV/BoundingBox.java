package readFromCSV;

import java.util.Locale;

public class BoundingBox {
    double xmin = Double.NaN;
    double ymin = Double.NaN;
    double xmax = Double.NaN;
    double ymax = Double.NaN;

    void addPoint(double x, double y){
        if(this.isEmpty()){
            this.xmin = x;
            this.xmax = x;
            this.ymin = y;
            this.ymax = y;
        }
        this.xmin = Math.min(this.xmin, x);
        this.xmax = Math.max(this.xmax, x);
        this.ymin = Math.min(this.ymin, y);
        this.ymax = Math.max(this.ymax, y);
    }

    boolean contains(double x, double y){
        if(this.isEmpty()) return false;
        if(x > this.xmax || x < this.xmin || y > this.ymax || y < this.ymin){
            return false;
        }
        return true;
    }

    boolean contains(BoundingBox bb){
        //jezeli jedno z nich jest puste to zwracam false
        if(this.isEmpty() || bb.isEmpty()) return false;
        return this.contains(bb.xmin, bb.ymin) && this.contains(bb.xmax, bb.ymax);
    }

    boolean intesects(BoundingBox bb){
        if(this.isEmpty() || bb.isEmpty()) return false;
        if(bb.xmin >= this.xmax || bb.xmax <= this.xmin || bb.ymin >= this.ymax || bb.ymax <= this.ymin) return false;
        return true;
    }


    boolean isEmpty(){
        if(Double.isNaN(this.xmin) || Double.isNaN(this.xmax) || Double.isNaN(this.ymin) || Double.isNaN(this.ymax)){
            return true;
        }
        return false;
    }

    BoundingBox add(BoundingBox bb){
        if(this.isEmpty()){
            this.xmin = Double.NaN;
            this.xmax = Double.NaN;
            this.ymin = Double.NaN;
            this.ymax = Double.NaN;
        }
        if(bb.isEmpty()) return this;
        //dodaje dwa punkty, ktore stworza box
        this.addPoint(bb.xmin, bb.ymin);
        this.addPoint(bb.xmax, bb.ymax);
        return this;
    }

    public boolean equals(Object o){
        if(!(o instanceof BoundingBox)) return false;
        BoundingBox bb = (BoundingBox)o;
        if(this.isEmpty() && bb.isEmpty()) return true;
        if(this.isEmpty() || bb.isEmpty()) return false;
        if(this.xmin == bb.xmin && this.xmax == bb.xmax && this.ymin == bb.ymin && this.ymax == bb.ymax){
            return true;
        }
        return false;
    }

    double getCenterX(){
        if(this.isEmpty()){
            throw new RuntimeException("Bounding box is empty");
        }
        return (this.xmin + this.xmax) / 2;
    }

    double getCenterY(){
        if(this.isEmpty()){
            throw new RuntimeException("Bounding box is empty");
        }
        return (this.ymin + this.ymax) / 2;
    }

    double disctanceTo(BoundingBox bb){
        if(this.isEmpty() || bb.isEmpty()){
            throw new RuntimeException("Bounding box is empty");
        }
        double centerX1 = this.getCenterX();
        double centerY1 = this.getCenterY();
        double centerX2 = bb.getCenterX();
        double centerY2 = bb.getCenterY();

        return haversine(centerY1, centerX1, centerY2, centerX2);
    }

    double haversine(double lat1, double lon1, double lat2, double lon2){
        //promien ziemi (w km)
        final double R = 6378.0;

        //wyznaczenie odległości
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public String toString(){
        if(this.isEmpty()){
            return "Empty bounding box";
        }
        return String.format(Locale.US, "Bounding box[xmin = %f, ymin = %f, xmax = %f, ymax = %f]", this.xmin, this.xmax, this.ymin, this.ymax);
    }

}