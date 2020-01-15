package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.GridHLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridPanelsModel {
    
    private final GridPanelsModelBehaviour behaviour = new GridPanelsModelBehaviour();
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

    public ObservableList<GridVLineModel> getGridVLines() {
        return behaviour.getGridVLines();
    }
    
    public ObservableList<GridHLineModel> getGridHLines() {
        return behaviour.getGridHLines();
    }

    public Observable<GridVLineModel> gridVLineAddedObservable() {
        return behaviour.gridVLineAddedObervable;
    }
    
    public Observable<GridHLineModel> gridHLineAddedObservable() {
        return behaviour.gridHLineAddedObservable;
    }
}