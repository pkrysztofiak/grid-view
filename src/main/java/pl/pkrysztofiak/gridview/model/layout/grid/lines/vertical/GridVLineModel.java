package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridVLineModel {

    private final GridVLineModelBehaviour behaviour;
    
    private final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    private final PublishSubject<Point2D> startDragRequest = PublishSubject.create();
    private final PublishSubject<Double> dragRequest = PublishSubject.create();
    
    public GridVLineModel(double ratioX, ObservableList<GridVLineModel> vGridLines, PanelModel... panels) {
        behaviour = new GridVLineModelBehaviour(ratioX, vGridLines, panels);
        initBehaviour();
    }
    
    public GridVLineModel(double ratioX, ObservableList<GridVLineModel> vGridLines, List<PanelModel> panels) {
        behaviour = new GridVLineModelBehaviour(ratioX, vGridLines, panels);
        initBehaviour();
    }
    
    private void initBehaviour() {
        addPanelRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onAddPanelRequest);
        startDragRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onStartDrag);
        dragRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onDrag);
    }
            
    public void add(PanelModel panel) {
        addPanelRequest.onNext(panel);
    }
    
    public Double getRatioX() {
        return behaviour.getRatioX();
    }
    
    public ObservableList<PanelVLineModel> getPanelsVLines() {
        return behaviour.getPanelsVLines();
    }
    
    public Observable<PanelVLineModel> panelVLineAddedObservable() {
        return behaviour.panelVLineAddedObservable;
    }
    
    public Observable<PanelVLineModel> panelVLineRemovedObservable() {
        return behaviour.panelVLineRemovedObservable;
    }
    
    public Observable<Double> ratioXObservable() {
        return behaviour.ratioXObservable;
    }
    
    public void startDrag(Point2D point) {
        startDragRequest.onNext(point);
    }
    
    public void drag(double newRatioX) {
        dragRequest.onNext(newRatioX);
    }
}