package pl.pkrysztofiak.gridview.model.hangingprotocol.grid;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VGridLinesModel {

    public final ObservableList<HangingProtocolVLine> vGridLines = FXCollections.observableArrayList();
    public final Observable<HangingProtocolVLine> vGridLineAddedObservable = JavaFxObservable.additionsOf(vGridLines);
    
    public VGridLinesModel() {
    }
    
    public Optional<HangingProtocolVLine> get(double ratioX) {
        return vGridLines.stream().filter(vGridLine -> vGridLine.getRatioX().equals(ratioX)).findFirst();
    }
    
    public void add(HangingProtocolVLine vGridLine) {
        vGridLines.add(vGridLine);
    }
}