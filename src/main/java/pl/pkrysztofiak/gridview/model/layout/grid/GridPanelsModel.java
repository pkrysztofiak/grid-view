package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.VGridLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.VGridLinesModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridPanelsModel {

    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    public final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);

    public final VGridLinesModel vGridLines = new VGridLinesModel();
        
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public GridPanelsModel(PanelModel... panels) {
        this.panels.setAll(panels);
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<VGridLineModel> optional = vGridLines.get(ratioX);
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
    
    public ObservableList<PanelModel> getPanels() {
        return panels;
    }
}
