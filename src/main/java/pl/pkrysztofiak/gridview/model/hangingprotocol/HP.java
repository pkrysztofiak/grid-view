package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.panels.Panel;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLinesModel;

public class HP {

    private final ObservableList<Panel> panels = FXCollections.observableArrayList();
    public final Observable<Panel> panelAddedObservable = JavaFxObservable.additionsOf(panels);

    public final VGridLinesModel vGridLines = new VGridLinesModel();
    
    private final ObservableList<VGridLineModel> vGridLines2 = FXCollections.observableArrayList();
        
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public HP(Panel... panels) {
        this.panels.setAll(panels);
    }
    
    private void onPanelAdded(Panel panel) {
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
    
    public ObservableList<Panel> getPanels() {
        return panels;
    }
}
