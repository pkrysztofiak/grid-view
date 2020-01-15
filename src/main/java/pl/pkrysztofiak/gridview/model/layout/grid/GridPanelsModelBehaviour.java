package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.GridHLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridPanelsModelBehaviour {

    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
 
    private final ObservableList<GridVLineModel> gridVLines = FXCollections.observableArrayList();
    private final ObservableList<GridVLineModel> unmodifiableGridVLines = FXCollections.unmodifiableObservableList(gridVLines);
    public final Observable<GridVLineModel> gridVLineAddedObervable = JavaFxObservable.additionsOf(gridVLines);
    
    private final ObservableList<GridHLineModel> gridHLines = FXCollections.observableArrayList();
    private final ObservableList<GridHLineModel> unmodifiableGridHLines = FXCollections.unmodifiableObservableList(gridHLines);
    public final Observable<GridHLineModel> gridHLineAddedObservable = JavaFxObservable.additionsOf(gridHLines);
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    public ObservableList<GridVLineModel> getGridVLines() {
        return unmodifiableGridVLines;
    }
    
    public ObservableList<GridHLineModel> getGridHLines() {
        return unmodifiableGridHLines;
    }
    
    void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<GridVLineModel> optional = findVGridLine(ratioX);
            if (optional.isPresent()) {
                GridVLineModel vGridLine = optional.get();
                vGridLine.addPanel.onNext(panel);
            } else {
                GridVLineModel vGridLine = new GridVLineModel(ratioX, gridVLines);
                vGridLine.addPanel.onNext(panel);
                gridVLines.add(vGridLine);
            }
        });
        
        Stream.of(panel.getRatioMinY(), panel.getRatioMaxY()).forEach(ratioY -> {
            Optional<GridHLineModel> optional = findHGridLine(ratioY);
            if (optional.isPresent()) {
                GridHLineModel gridHLine = optional.get();
                gridHLine.addPanel.onNext(panel);
            } else {
                GridHLineModel gridHLine = new GridHLineModel(ratioY, gridHLines);
                gridHLine.addPanel.onNext(panel);
                gridHLines.add(gridHLine);
            }
        });
    }
    
    private Optional<GridVLineModel> findVGridLine(double x) {
        return gridVLines.stream().filter(gridVLine -> gridVLine.getRatioX().equals(x)).findFirst();
    }
    
    private Optional<GridHLineModel> findHGridLine(double y) {
        return gridHLines.stream().filter(gridHLine -> gridHLine.getRatioY().equals(y)).findFirst();
    }
}