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

        Observable.fromIterable(gridVLineModel.getVLines()).subscribe(this::onVLineModelAdded);
        gridVLineModel.vLineAddedObservable.subscribe(this::onVLineModelAdded);
        
        Observable.combineLatest(gridVLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width)
        .subscribe(x -> setLayoutX(x));
        
        prefHeightProperty().bind(panelsView.heightProperty());
        
        pressedXObservable.switchMap(pressedX -> 
            Observable.combineLatest(draggedXObservable, Observable.just(gridVLineModel.getRatioX()), (draggedX, ratioX) -> ratioX + (draggedX - pressedX) / panelsView.getWidth()))
        .subscribe(gridVLineModel.drag::onNext);
    }
    
    private void onVLineModelAdded(VLineModel vLineModel) {
        Platform.runLater(() -> {
            VLineView vLineView = new VLineView(vLineModel);
            
            lines.add(vLineView);
            
            //TODO dopisać takeUntil()
            Observable.combineLatest(vLineModel.ratioMinYObservable, gridPanelsView.heightObservable, (ratioMinY, height) -> ratioMinY * height).subscribe(vLineView::setStartY);
            Observable.combineLatest(vLineModel.ratioMaxYObservable, gridPanelsView.heightObservable, (ratioMaxY, height) -> ratioMaxY * height).subscribe(vLineView::setEndY);
            
//            gridPanelsView.heightObservable.subscribe(height -> {
//                vLineView.setStartY(height * vLineModel.getRatioMinY());
//                vLineView.setEndY(height * vLineModel.getRatioMaxY());
//            });
            
            gridVLineModel.vLineRemovedObservable.filter(vLineModel::equals).subscribe(removedPanelVLineModel -> lines.remove(vLineView));
        });
    }
    
    
    private void onDragStarted(Point2D screenPoint) {
        Point2D point = gridPanelsView.screenToLocal(screenPoint);
        gridVLineModel.startDrag.onNext(new Point2D(point.getX() / gridPanelsView.getWidth(), point.getY() / gridPanelsView.getHeight()));
    }
}