package pl.pkrysztofiak.gridview.view.layout.grid.lines.vertical;

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
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.VLineModel;
import pl.pkrysztofiak.gridview.view.layout.grid.GridPanelsView;

public class GridVLineView extends Pane {
    
    private final GridPanelsView gridPanelsView;
    private final GridVLineModel gridVLineModel;
    
    private final ObservableList<Line> lines = FXCollections.observableArrayList();
    private final Observable<Double> pressedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_PRESSED).map(MouseEvent::getScreenX);
    private final Observable<Double> draggedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_DRAGGED).map(MouseEvent::getScreenX);
    private final Observable<Double> releasedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_RELEASED).map(MouseEvent::getScreenX);

    private final Observable<Point2D> mousePressedObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_PRESSED).map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
    private final Observable<Point2D> mouseDraggedObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_DRAGGED).map(event -> new Point2D(event.getScreenX(), event.getScreenY()));
    
    {
        setMinWidth(0);
        setPrefWidth(0.);
        setMaxWidth(0);
        
        mousePressedObservable.switchMap(pressed -> mouseDraggedObservable.map(dragged -> pressed).take(1)).subscribe(this::onDragStarted);
    }
    
    public GridVLineView(GridVLineModel gridVLineModel, GridPanelsView panelsView) {
        this.gridVLineModel = gridVLineModel;
        this.gridPanelsView = panelsView;
        Bindings.bindContent(getChildren(), lines);

        Observable.fromIterable(gridVLineModel.getVLines()).subscribe(this::onPanelVLineModelAdded);
        gridVLineModel.vLineAddedObservable.subscribe(this::onPanelVLineModelAdded);
        
        Observable.combineLatest(gridVLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width)
        .subscribe(x -> setLayoutX(x));
        
        prefHeightProperty().bind(panelsView.heightProperty());
        
        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(draggedXObservable, Observable.just(gridVLineModel.getRatioX()), (draggedX, ratioX) -> {
            return ratioX + (draggedX - pressedX) / panelsView.getWidth();
        })).subscribe(newRatioX -> {
//            gridVLineModel.drag(newRatioX);
            gridVLineModel.drag.onNext(newRatioX);
        });
    }
    
    private void onPanelVLineModelAdded(VLineModel panelVLineModel) {
        Platform.runLater(() -> {
            VLineView panelVLineView = new VLineView(panelVLineModel);
            
            lines.add(panelVLineView);
            
            //TODO dopisać takeUntil()
            gridPanelsView.heightObservable.subscribe(height -> {
                panelVLineView.setStartY(height * panelVLineModel.getRatioMinY());
                panelVLineView.setEndY(height * panelVLineModel.getRatioMaxY());
            });
            
            gridVLineModel.vLineRemovedObservable.filter(panelVLineModel::equals).subscribe(removedPanelVLineModel -> lines.remove(panelVLineView));
        });
    }
    
    
    private void onDragStarted(Point2D screenPoint) {
        Point2D point = gridPanelsView.screenToLocal(screenPoint);
//        gridVLineModel.startDrag(new Point2D(point.getX() / gridPanelsView.getWidth(), point.getY() / gridPanelsView.getHeight()));
        gridVLineModel.startDrag.onNext(new Point2D(point.getX() / gridPanelsView.getWidth(), point.getY() / gridPanelsView.getHeight()));
    }
}