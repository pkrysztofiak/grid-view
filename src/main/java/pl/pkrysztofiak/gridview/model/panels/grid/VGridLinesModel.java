package pl.pkrysztofiak.gridview.model.panels.grid;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VGridLinesModel {

    public final ObservableList<VGridLineModel> vGridLines = FXCollections.observableArrayList();
    public final Observable<VGridLineModel> vGridLineAddedObservable = JavaFxObservable.additionsOf(vGridLines);
    
    public Optional<VGridLineModel> get(double ratioX) {
        return vGridLines.stream().filter(vGridLine -> vGridLine.getRatioX().equals(ratioX)).findFirst();
    }
    
    public void add(VGridLineModel vGridLine) {
        vGridLines.add(vGridLine);
    }
}