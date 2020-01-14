package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Behaviour {

    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
 
    private final ObservableList<GridVLineModel> vGridLines = FXCollections.observableArrayList();
    private final ObservableList<GridVLineModel> unmodifiableVGridLines = FXCollections.unmodifiableObservableList(vGridLines);
    public final Observable<GridVLineModel> vGridLineAdded = JavaFxObservable.additionsOf(vGridLines);
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    public ObservableList<GridVLineModel> getVGridLines() {
        return unmodifiableVGridLines;
    }
    
    void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<GridVLineModel> optional = findVGridLine(ratioX);
            if (optional.isPresent()) {
                GridVLineModel vGridLine = optional.get();
                vGridLine.add(panel);
            } else {
                GridVLineModel vGridLine = new GridVLineModel(ratioX, vGridLines);
                vGridLine.add(panel);
                vGridLines.add(vGridLine);
            }
        });
    }
    
    private Optional<GridVLineModel> findVGridLine(double x) {
        return vGridLines.stream().filter(vGridLine -> vGridLine.getRatioX().equals(x)).findFirst();
    }
}