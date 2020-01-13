package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.hangingprotocol.grid.VGridLine;
import pl.pkrysztofiak.gridview.model.hangingprotocol.grid.VGridLinesModel;
import pl.pkrysztofiak.gridview.model.panels.Panel;

public class HangingProtocol {

    private final ObservableList<Panel> panels = FXCollections.observableArrayList();
    public final Observable<Panel> panelAddedObservable = JavaFxObservable.additionsOf(panels);

    public final VGridLinesModel vGridLines = new VGridLinesModel();
    
    private final ObservableList<VGridLine> vGridLines2 = FXCollections.observableArrayList();
        
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public HangingProtocol(Panel... panels) {
        this.panels.setAll(panels);
    }
    
    private void onPanelAdded(Panel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<VGridLine> optional = vGridLines.get(ratioX);
            if (optional.isPresent()) {
                VGridLine vGridLine = optional.get();
                vGridLine.add(panel);
            } else {
                VGridLine vGridLine = new VGridLine(ratioX, vGridLines);
                vGridLine.add(panel);
                vGridLines.add(vGridLine);
            }
        });
    }
    
    public ObservableList<Panel> getPanels() {
        return panels;
    }
}
