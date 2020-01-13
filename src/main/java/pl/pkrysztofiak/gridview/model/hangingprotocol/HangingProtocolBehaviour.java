package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.hangingprotocol.grid.HangingProtocolVLine;
import pl.pkrysztofiak.gridview.model.panels.Panel;

public class HangingProtocolBehaviour {
    
    private final ObservableList<Panel> panels = FXCollections.observableArrayList();
    private Observable<Panel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
    public final ObservableList<Panel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
    
    private final ObservableList<HangingProtocolVLine> vLines = FXCollections.observableArrayList();
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public HangingProtocolBehaviour(HangingProtocol hangingProtocol) {
        
    }
    
    private void onPanelAddRequest(Panel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(Panel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<HangingProtocolVLine> optional = vGridLines.get(ratioX);
            if (optional.isPresent()) {
                HangingProtocolVLine vGridLine = optional.get();
                vGridLine.add(panel);
            } else {
                HangingProtocolVLine vGridLine = new HangingProtocolVLine(ratioX, vGridLines);
                vGridLine.add(panel);
                vGridLines.add(vGridLine);
            }
        });
    }
    

}
