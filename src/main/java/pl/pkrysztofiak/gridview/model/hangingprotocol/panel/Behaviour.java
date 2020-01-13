package pl.pkrysztofiak.gridview.model.hangingprotocol.panel;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

class Behaviour {

public final PublishSubject<PanelModel> addPanelRequestPublishable = PublishSubject.create();
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
 
    {
        addPanelRequestPublishable.delay(0, TimeUnit.SECONDS, Schedulers.single()).subscribe(this::onAddPanelRequest);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    private void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
}
