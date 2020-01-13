package pl.pkrysztofiak.gridview.model.panels;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLinesModel;

public class PanelsModel {

    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    public final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);

    public final VGridLinesModel vGridLines = new VGridLinesModel();
    
    private final ObservableList<VGridLineModel> vGridLines2 = FXCollections.observableArrayList();
        
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public PanelsModel(PanelModel... panels) {
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
