package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PanelVLineModel {

    private final ObjectProperty<Double> ratioMinYProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> ratioMaxYProperty = new SimpleObjectProperty<>();

    public PanelVLineModel() {
    }
    
    public PanelVLineModel(double ratioMinY, double ratioMaxY) {
        ratioMinYProperty.set(ratioMinY);
        ratioMaxYProperty.set(ratioMinY);
    }
    
    public Double getRatioMinY() {
        return ratioMinYProperty.get();
    }
    
    public void setRationMinY(double value) {
        ratioMinYProperty.set(value);
    }
    
    public Double getRatioMaxY() {
        return ratioMaxYProperty.get();
    }
    
    public void setRationMaxY(double value) {
        ratioMaxYProperty.set(value);
    }
}