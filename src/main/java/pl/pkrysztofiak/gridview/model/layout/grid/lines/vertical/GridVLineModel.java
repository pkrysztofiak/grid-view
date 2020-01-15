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
    
    private final PublishSubject<VLineModel> addVLine = PublishSubject.create();
    
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
        
        //NEW
        addVLine.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::addVLine);
    }
            
    public void add(PanelModel panel) {
        addPanelRequest.onNext(panel);
    }
    
    public void add(VLineModel vLine) {
        addVLine.onNext(vLine);
    }
    
    public Double getRatioX() {
        return behaviour.getRatioX();
    }
    
    public ObservableList<VLineModel> getPanelsVLines() {
        return behaviour.getVLines();
    }
    
    public Observable<VLineModel> panelVLineAddedObservable() {
        return behaviour.vLineAddedObservable;
    }
    
    public Observable<VLineModel> panelVLineRemovedObservable() {
        return behaviour.vLineRemovedObservable;
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