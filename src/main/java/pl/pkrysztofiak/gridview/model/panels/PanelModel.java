package pl.pkrysztofiak.gridview.model.panels;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class PanelModel {

    public final DoubleProperty ratioMinXProperty = new SimpleDoubleProperty();
    public final Observable<Double> ratioMinXObservable = JavaFxObservable.valuesOf(ratioMinXProperty).map(Number::doubleValue);
    
    public final DoubleProperty ratioMinYProperty = new SimpleDoubleProperty();
    public final Observable<Double> ratioMinYObservable = JavaFxObservable.valuesOf(ratioMinYProperty).map(Number::doubleValue);
    
    public final DoubleProperty ratioMaxXProperty = new SimpleDoubleProperty();
    public final Observable<Double> ratioMaxXObservable = JavaFxObservable.valuesOf(ratioMaxXProperty).map(Number::doubleValue);
    
    public final DoubleProperty ratioMaxYProperty = new SimpleDoubleProperty();
    public final Observable<Double> ratioMaxYObservable = JavaFxObservable.valuesOf(ratioMaxYProperty).map(Number::doubleValue);
    
    
    public PanelModel(double minX, double minY, double maxX, double maxY) {
        ratioMinXProperty.set(minX);
        ratioMinYProperty.set(minY);
        ratioMaxXProperty.set(maxX);
        ratioMaxYProperty.set(maxY);
        System.out.println("PanelModel created=" + toString());
    }
    
    public Double getRatioMinX() {
        return ratioMinXProperty.getValue();
    }
    
    public void setRatioMinX(double value) {
        ratioMinXProperty.set(value);
    }
    
    public Double getRatioMinY() {
        return ratioMinYProperty.getValue();
    }
    
    public Double getRatioMaxX() {
        return ratioMaxXProperty.getValue();
    }
    
    public Double getRatioMaxY() {
        return ratioMaxYProperty.getValue();
    }
    
    @Override
    public String toString() {
        return "PanelModel[minX=" + ratioMinXProperty.get() + ", minY=" + ratioMinYProperty.get() + ", maxX=" + ratioMaxXProperty.get() + ", maxY=" + ratioMaxYProperty.get() + "]";
    }
    
    public boolean isVerticallyConnected(PanelModel panel) {
        System.out.println();
        System.out.println("this=" + this);
        System.out.println("panel=" + panel);
        boolean condition1 = panel.getRatioMaxY() < ratioMinYProperty.get();
        boolean condition2 = ratioMaxYProperty.get() < panel.getRatioMinY();
        boolean combined = !(condition1 || condition2);
        System.out.println("condition1=" + condition1 + ", condition2=" + condition2);
        System.out.println("combined conditions=" + combined);
        System.out.println();
        return combined; 
    }
}