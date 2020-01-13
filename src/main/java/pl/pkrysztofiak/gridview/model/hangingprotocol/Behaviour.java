package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.hangingprotocol.panel.HangingProtocolPanel;

class Behaviour {

    public final PublishSubject<HangingProtocolPanel> addHpPanelRequest = PublishSubject.create();
    
    private final ObservableList<HangingProtocolPanel> hpPanels = FXCollections.observableArrayList();
    private final ObservableList<HangingProtocolPanel> unmodifiableHpPanels = FXCollections.unmodifiableObservableList(hpPanels);
    private final Observable<HangingProtocolPanel> hpPanelAddedObservable = JavaFxObservable.additionsOf(hpPanels);
    
    {
        addHpPanelRequest.delay(0, TimeUnit.SECONDS, Schedulers.single()).subscribe(this::onAddHpPanelRequest);
    }
    
    public ObservableList<HangingProtocolPanel> getHpPanels() {
        return unmodifiableHpPanels;
    }
    
    private void onAddHpPanelRequest(HangingProtocolPanel hpPanel) {
        hpPanels.add(hpPanel);
    }
}