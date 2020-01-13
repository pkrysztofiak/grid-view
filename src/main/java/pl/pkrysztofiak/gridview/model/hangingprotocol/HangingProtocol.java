package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.hangingprotocol.grid.HangingProtocolVLine;
import pl.pkrysztofiak.gridview.model.hangingprotocol.grid.VGridLinesModel;
import pl.pkrysztofiak.gridview.model.panels.Panel;

public class HangingProtocol {

    private final Behaviour behaviour = new Behaviour();
    
    public final VGridLinesModel vGridLines = new VGridLinesModel();
    
    private final PublishSubject<Panel> addPanelRequestPublishable = PublishSubject.create();
    
    private final ObservableList<HangingProtocolVLine> vGridLines2 = FXCollections.observableArrayList();
        
    {
        addPanelRequestPublishable.delay(0, TimeUnit.SECONDS, Schedulers.single()).subscribe(behaviour::onPanelAddRequest);
    }
    
    public HangingProtocol(Panel... panels) {
        Stream.of(panels).forEach(addPanelRequestPublishable::onNext);
    }
    
    public ObservableList<Panel> getPanels() {
        return behaviour.unmodifiablePanels;
    }
    
    private class Behaviour {
        
        private final ObservableList<Panel> panels = FXCollections.observableArrayList();
        private Observable<Panel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
        public final ObservableList<Panel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
        
        {
            panelAddedObservable.subscribe(this::onPanelAdded);
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
}
