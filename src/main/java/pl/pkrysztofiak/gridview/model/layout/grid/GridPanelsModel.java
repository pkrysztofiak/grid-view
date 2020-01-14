package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridPanelsModel {
    
    private final Behaviour behaviour = new Behaviour();
    public final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    
    {
        addPanelRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onAddPanelRequest);
    }
    
    public GridPanelsModel(PanelModel... panels) {
        Stream.of(panels).forEach(addPanelRequest::onNext);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return behaviour.getPanels();
    }

    public ObservableList<VGridLineModel> getVGridLines() {
        return behaviour.getVGridLines();
    }

    public Observable<VGridLineModel> vGridLineAdded() {
        return behaviour.vGridLineAdded;
    }
}