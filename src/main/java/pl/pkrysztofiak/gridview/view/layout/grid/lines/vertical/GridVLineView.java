package pl.pkrysztofiak.gridview.view.layout.grid.lines.vertical;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.PanelVLineModel;
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

        Observable.fromIterable(gridVLineModel.getPanelsVLines()).subscribe(panelVLineModel -> {
            
        });
        
        
        
        Observable.fromIterable(gridVLineModel.getVLines()).delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVLineAdded);
        gridVLineModel.vLineAddedObservable().delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVLineAdded);
        
        Observable.combineLatest(gridVLineModel.ratioXObservable(), panelsView.widthObservable, (ratioX, width) -> ratioX * width)
        .subscribe(x -> setLayoutX(x));
        
        prefHeightProperty().bind(panelsView.heightProperty());
        
        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(draggedXObservable, Observable.just(gridVLineModel.getRatioX()), (draggedX, ratioX) -> {
            return ratioX + (draggedX - pressedX) / panelsView.getWidth();
        })).subscribe(newRatioX -> {
            gridVLineModel.drag(newRatioX);
        });
    }
    
    private void onPanelVLineModelAdded(PanelVLineModel panelVLineModel) {
        Line line = new Line();
        line.setStroke(Color.GREEN);
        line.setStrokeWidth(8.);

        lines.add(line);
        
        gridPanelsView.heightObservable.subscribe(height -> {
            line.setStartY(height * panelVLineModel.getRatioMinY());
            line.setEndY(height * panelVLineModel.getRatioMaxY());
        });
    }
    
    private void onDragStarted(Point2D screenPoint) {
        Point2D point = gridPanelsView.screenToLocal(screenPoint);
        gridVLineModel.startDrag(new Point2D(point.getX() / gridPanelsView.getWidth(), point.getY() / gridPanelsView.getHeight()));
    }
    
    private void onVLineAdded(Line2D modelLine) {
        Line line = new Line();
        line.setStroke(Color.RED);
        line.setStrokeWidth(8);
        lines.add(line);
        
        gridVLineModel.vLineRemovedObservable().filter(modelLine::equals).subscribe(removedModelLine -> {
            lines.remove(line);
        });
        
        gridPanelsView.heightObservable.subscribe(height -> {
            line.setStartY(height * modelLine.getStartY());
            line.setEndY(height * modelLine.getEndY());
        });
    }
    
}