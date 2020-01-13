package pl.pkrysztofiak.gridview.view.panels.grid;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.view.panels.PanelsView;

public class VGridLineView extends Pane {
    
    private final PanelsView panelsView;
    private final VGridLineModel vGridLineModel;
    
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
    
    public VGridLineView(VGridLineModel vGridLineModel, PanelsView panelsView) {
        this.vGridLineModel = vGridLineModel;
        this.panelsView = panelsView;
        Bindings.bindContent(getChildren(), lines);

        vGridLineModel.getLines().forEach(this::onLineAdded);
        vGridLineModel.modelLineAddedObservable.subscribe(this::onLineAdded);
        
        Observable.combineLatest(vGridLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width).subscribe(x -> {
            setLayoutX(x);
        });
        
        prefHeightProperty().bind(panelsView.heightProperty());
        
        
        
        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(draggedXObservable, Observable.just(vGridLineModel.getRatioX()), (draggedX, ratioX) -> {
            return ratioX + (draggedX - pressedX) / panelsView.getWidth();
        })).subscribe(newRatioX -> {
            vGridLineModel.dragPublishable.onNext(newRatioX);
        });
    }
    
    private void onDragStarted(Point2D screenPoint) {
        Point2D point = panelsView.screenToLocal(screenPoint);
        vGridLineModel.startDrag(new Point2D(point.getX() / panelsView.getWidth(), point.getY() / panelsView.getHeight()));
    }
    
    private void onLineAdded(Line2D modelLine) {
        Line line = new Line();
        line.setStroke(Color.RED);
        line.setStrokeWidth(8);
        lines.add(line);
        
        vGridLineModel.modelLineRemovedObservable.filter(modelLine::equals).subscribe(removedModelLine -> {
            lines.remove(line);
        });
        
        panelsView.heightObservable.subscribe(height -> {
            line.setStartY(height * modelLine.getStartY());
            line.setEndY(height * modelLine.getEndY());
        });
    }
    
}