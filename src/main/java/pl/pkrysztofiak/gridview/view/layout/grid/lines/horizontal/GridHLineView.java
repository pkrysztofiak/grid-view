package pl.pkrysztofiak.gridview.view.layout.grid.lines.horizontal;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.GridHLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.HLineModel;
import pl.pkrysztofiak.gridview.view.layout.grid.GridPanelsView;

public class GridHLineView extends Pane {

    private final GridHLineModel gridHLineModel;
    private final GridPanelsView gridPanelsView;
    
    private final ObservableList<Line> lines = FXCollections.observableArrayList();
    private final Observable<Double> pressedYObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_PRESSED).map(MouseEvent::getScreenY);
    private final Observable<Double> draggedYObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_DRAGGED).map(MouseEvent::getScreenY);
    private final Observable<Double> releasedYObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_RELEASED).map(MouseEvent::getScreenY);

    private final Observable<Point2D> mousePressedObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_PRESSED).map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
    private final Observable<Point2D> mouseDraggedObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_DRAGGED).map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
    
    {
        setMinHeight(0);
        setPrefHeight(0.);
        setMaxHeight(0);
        
        mousePressedObservable.switchMap(pressed -> mouseDraggedObservable.map(dragged -> pressed).take(1)).subscribe(this::onDragStarted);
    }
    
    public GridHLineView(GridHLineModel gridHLineModel, GridPanelsView gridPanelsView) {
        this.gridHLineModel = gridHLineModel;
        this.gridPanelsView = gridPanelsView;
        Bindings.bindContent(getChildren(), lines);
        
        Observable.fromIterable(gridHLineModel.getHLines()).subscribe(this::onHLineModelAdded);
        gridHLineModel.hLineAddedObservable.subscribe(this::onHLineModelAdded);
        
        Observable.combineLatest(gridHLineModel.ratioYObservable, gridPanelsView.heightObservable, (ratioY, hegiht) -> ratioY * hegiht)
        .subscribe(this::setLayoutY);
        
        prefWidthProperty().bind(gridPanelsView.widthProperty());
        
        pressedYObservable.switchMap(pressedX -> 
            Observable.combineLatest(draggedYObservable, Observable.just(gridHLineModel.getRatioY()), (draggedX, ratioX) -> ratioX + (draggedX - pressedX) / gridPanelsView.getWidth()))
        .subscribe(gridHLineModel.drag::onNext);
    }
    
    private void onHLineModelAdded(HLineModel hLineModel) {
        Platform.runLater(() -> {
            
            HLineView hLineView = new HLineView(hLineModel);
            lines.add(hLineView);
            
            //TODO dopisać takeUntil()
            gridPanelsView.widthObservable.subscribe(width -> {
                hLineView.setStartX(width * hLineModel.getRatioMinX());
                hLineView.setEndX(width * hLineModel.getRatioMaxX());
            });
            
            System.out.println("hlineView=" + hLineView);
            
            gridHLineModel.hLineRemovedObservable.filter(hLineModel::equals).subscribe(removedPanelVLineModel -> lines.remove(hLineView));
        });
    }
    
    private void onDragStarted(Point2D screenPoint) {
        Point2D point = gridPanelsView.screenToLocal(screenPoint);
        gridHLineModel.startDrag.onNext(new Point2D(point.getY() / gridPanelsView.getWidth(), point.getY() / gridPanelsView.getHeight()));
    }
}
