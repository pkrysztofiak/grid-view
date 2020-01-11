package pl.pkrysztofiak.gridview.model.panels.grid;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class VGridLineModel {

    private final ObjectProperty<Double> ratioXProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioXObservable = JavaFxObservable.valuesOf(ratioXProperty);
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels); 
    private final Observable<PanelModel> panelRemovedObservable = JavaFxObservable.removalsOf(panels);
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public VGridLineModel(double ratioX) {
        ratioXProperty.set(ratioX);
    }
    
    public Double getRatioX() {
        return ratioXProperty.get();
    }
    
    public void add(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        if (panel.getRatioMinX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(x -> panel.ratioMinXProperty.set(x));
            panel.ratioMinXObservable.filter(panelMinX -> !panelMinX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMinX -> panels.remove(panel));
            
        } else if (panel.getRatioMaxX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(x -> panel.ratioMaxXProperty.set(x));
            panel.ratioMaxXObservable.filter(panelMaxX -> !panelMaxX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMaxX -> panels.remove(panel));
        } else {
            throw new RuntimeException();
        }
    }
}
