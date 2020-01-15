package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class VLineModel implements Comparable<VLineModel> {

    private final PanelModel panel;
    
    private final ObjectProperty<Double> ratioMinYProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioMinYObservable = JavaFxObservable.valuesOf(ratioMinYProperty);
    
    private final ObjectProperty<Double> ratioMaxYProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioMaxYObservable = JavaFxObservable.valuesOf(ratioMaxYProperty);
    
    public VLineModel(PanelModel panel) {
        this.panel = panel;
    }
    
    public Double getRatioMinY() {
        return ratioMinYProperty.get();
    }
    
    public PanelModel getPanel() {
        return panel;
    }

    public void setRatioMinY(double value) {
        ratioMinYProperty.set(value);
    }
    
    public Double getRatioMaxY() {
        return ratioMaxYProperty.get();
    }
    
    public void setRationMaxY(double value) {
        ratioMaxYProperty.set(value);
    }
    
    public boolean contains(double ratioY) {
        return ratioY >= ratioMinYProperty.get() && ratioY <= ratioMaxYProperty.get();
    }
    
    public boolean isConnected(VLineModel vLine) {
        return !(vLine.getRatioMaxY() < ratioMinYProperty.get() || vLine.getRatioMinY() > ratioMaxYProperty.get());
    }

    @Override
    public int compareTo(VLineModel vLine) {
        int result = ratioMinYProperty.get().compareTo(vLine.getRatioMinY());
        if (result == 0) {
            result = ratioMaxYProperty.get().compareTo(vLine.getRatioMaxY());
        }
        return result;
    }
}