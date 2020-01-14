package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class VGridLineModel {

    private final Behaviour behaviour;
    
    private final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    private final PublishSubject<Point2D> startDragRequest = PublishSubject.create();
    
    public VGridLineModel(double ratioX, ObservableList<VGridLineModel> vGridLines, PanelModel... panels) {
        behaviour = new Behaviour(ratioX, vGridLines, panels);
        initBehaviour();
    }
    
    public VGridLineModel(double ratioX, ObservableList<VGridLineModel> vGridLines, List<PanelModel> panels) {
        behaviour = new Behaviour(ratioX, vGridLines, panels);
        initBehaviour();
    }
    
    private void initBehaviour() {
        addPanelRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onAddPanelRequest);
        startDragRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(behaviour::onStartDrag);
    }
            
    public void add(PanelModel panel) {
        addPanelRequest.onNext(panel);
    }
    
    public Double getRatioX() {
        return behaviour.getRatioX();
    }
    
    public ObservableSet<Line2D> getVLines() {
        return behaviour.getVLines();
    }
    
    public Observable<Line2D> vLineAddedObservable() {
        return behaviour.vLineAddedObservable;
    }
    
    public Observable<Line2D> vLineRemovedObservable() {
        return behaviour.vLineRemovedObservable;
    }
    
    public Observable<Double> ratioXObservable() {
        return behaviour.ratioXObservable;
    }
    
    public void startDrag(Point2D point) {
        startDragRequest.onNext(point);
//        behaviour.startDragRequest.onNext(point);
    }
    
    public void drag(double newRatioX) {
        behaviour.dragRequest.onNext(newRatioX);
    }
}