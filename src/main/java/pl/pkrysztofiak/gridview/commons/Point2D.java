package pl.pkrysztofiak.gridview.commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Point2D {

    private final ObjectProperty<Double> xProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> yProperty = new SimpleObjectProperty<>();
    
    public Point2D(double x, double y) {
        xProperty.set(x);
        yProperty.set(y);
    }
 
    public Double getX() {
        return xProperty.get();
    }
    
    public void setX(double x) {
        xProperty.set(x);
    }
    
    public Double getY() {
        return xProperty.get();
    }
    
    public void setY(double y) {
        yProperty.set(y);
    }
}