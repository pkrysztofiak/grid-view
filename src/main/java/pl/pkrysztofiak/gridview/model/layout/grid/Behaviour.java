package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Behaviour {

    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
 
    private final ObservableList<VGridLineModel> vGridLines = FXCollections.observableArrayList();
    private final ObservableList<VGridLineModel> unmodifiableVGridLines = FXCollections.unmodifiableObservableList(vGridLines);
    public final Observable<VGridLineModel> vGridLineAdded = JavaFxObservable.additionsOf(vGridLines);
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    public ObservableList<VGridLineModel> getVGridLines() {
        return unmodifiableVGridLines;
    }
    
    void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<VGridLineModel> optional = findVGridLine(ratioX);
            if (optional.isPresent()) {
                VGridLineModel vGridLine = optional.get();
                vGridLine.add(panel);
            } else {
                VGridLineModel vGridLine = new VGridLineModel(ratioX, vGridLines);
                vGridLine.add(panel);
                vGridLines.add(vGridLine);
            }
        });
    }
    
    private Optional<VGridLineModel> findVGridLine(double x) {
        return vGridLines.stream().filter(vGridLine -> vGridLine.getRatioX().equals(x)).findFirst();
    }
}