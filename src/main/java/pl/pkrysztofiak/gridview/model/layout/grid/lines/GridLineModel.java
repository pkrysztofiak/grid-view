package pl.pkrysztofiak.gridview.model.layout.grid.lines;

import java.util.concurrent.TimeUnit;

import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public abstract class GridLineModel {

    public final PublishSubject<PanelModel> addPanel = PublishSubject.create();
    public final PublishSubject<Point2D> startDrag = PublishSubject.create();
    public final PublishSubject<Double> drag = PublishSubject.create();
    
    {
        addPanel.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::addPanel);
        startDrag.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::startDrag);
        drag.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(value -> this.drag(value));
    }
    
    protected abstract void addPanel(PanelModel panel);
    
    protected abstract void startDrag(Point2D point);
    
    protected abstract void drag(double ratioX);
}
