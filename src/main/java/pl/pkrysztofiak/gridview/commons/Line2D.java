package pl.pkrysztofiak.gridview.commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Line2D {

    private final ObjectProperty<Double> startXProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> startYProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> endXProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> endYProperty = new SimpleObjectProperty<>();
    
    public Line2D(double startX, double startY, double endX, double endY) {
        startXProperty.set(startX);
        startYProperty.set(startY);
        endXProperty.set(endX);
        endYProperty.set(endY);
    }
    
    public void setStartX(double x) {
        startXProperty.set(x);
    }
    
    public Double getStartX() {
        return startXProperty.get();
    }
    
    public void setStartY(double y) {
        startYProperty.set(y);
    }
    
    public Double getStartY() {
        return startYProperty.get();
    }
    
    public void setEndX(double x) {
        endXProperty.set(x);
    }
    
    public Double getEndX() {
        return endXProperty.get();
    }
    
    public void setEndY(double y) {
        endYProperty.set(y);
    }
    
    public Double getEndY() {
        return endYProperty.get();
    }
}