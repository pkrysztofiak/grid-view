package pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class HLineModel implements Comparable<HLineModel> {

    private final PanelModel panel;
    
    private final ObjectProperty<Double> ratioMinXProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioMinXObservable = JavaFxObservable.valuesOf(ratioMinXProperty);
    
    private final ObjectProperty<Double> ratioMaxXProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioMaxXObservable = JavaFxObservable.valuesOf(ratioMaxXProperty);
    
    public HLineModel(PanelModel panel) {
        this.panel = panel;
    }
    
    public Double getRatioMinX() {
        return ratioMinXProperty.get();
    }
    
    public PanelModel getPanel() {
        return panel;
    }

    public void setRatioMinX(double value) {
        ratioMinXProperty.set(value);
    }
    
    public Double getRatioMaxX() {
        return ratioMaxXProperty.get();
    }
    
    public void setRationMaxX(double value) {
        ratioMaxXProperty.set(value);
    }
    
    public boolean contains(double ratioX) {
        return ratioX >= ratioMinXProperty.get() && ratioX <= ratioMaxXProperty.get();
    }
    
    public boolean isConnected(HLineModel hLine) {
        return !(hLine.getRatioMaxX() < ratioMinXProperty.get() || hLine.getRatioMinX() > ratioMaxXProperty.get());
    }

    @Override
    public int compareTo(HLineModel hLine) {
        int result = ratioMinXProperty.get().compareTo(hLine.getRatioMinX());
        if (result == 0) {
            result = ratioMaxXProperty.get().compareTo(hLine.getRatioMaxX());
        }
        return result;
    }
    
    @Override
    public String toString() {
        return "HLine[minX=" + ratioMinXProperty.get() + ", maxX=" + ratioMaxXProperty.get() + "]";
    }
}
