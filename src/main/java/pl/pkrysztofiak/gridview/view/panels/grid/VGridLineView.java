package pl.pkrysztofiak.gridview.view.panels.grid;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.view.panels.PanelsView;

public class VGridLineView extends Pane {
    
    private final PanelsView panelsView;
    private final VGridLineModel vGridLineModel;
    
    private final ObservableList<Line> lines = FXCollections.observableArrayList();
    private final Observable<Double> pressedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_PRESSED).map(MouseEvent::getScreenX);
    private final Observable<Double> draggedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_DRAGGED).map(MouseEvent::getScreenX);
    private final Observable<Double> releasedXObservable = JavaFxObservable.eventsOf(this, MouseEvent.MOUSE_RELEASED).map(MouseEvent::getScreenX);
    
    {
        setMinWidth(0);
        setPrefWidth(0.);
        setMaxWidth(0);
    }
    
    public VGridLineView(VGridLineModel vGridLineModel, PanelsView panelsView) {
        this.vGridLineModel = vGridLineModel;
        this.panelsView = panelsView;
        Bindings.bindContent(getChildren(), lines);

        vGridLineModel.getLines().forEach(modelLine -> {
            
            Line line = new Line();
            line.setStroke(Color.RED);
            line.setStrokeWidth(8);
            lines.add(line);
            
            panelsView.heightObservable.subscribe(height -> {
                line.setStartY(height * modelLine.getStartY());
                line.setEndY(height * modelLine.getEndY());
            });
        });
        
        Observable.combineLatest(vGridLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width).subscribe(x -> {
            setLayoutX(x);
        });
        
        prefHeightProperty().bind(panelsView.heightProperty());
        
//        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(draggedXObservable, Observable.just(vGridLineModel.getRatioX()), (x, y) -> 1)).subscribe(next -> {
//            
//        });
        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(draggedXObservable, Observable.just(vGridLineModel.getRatioX()), (draggedX, ratioX) -> {
            return ratioX + (draggedX - pressedX) / panelsView.getWidth();
        })).subscribe(newRatioX -> {
            vGridLineModel.dragPublishable.onNext(newRatioX);
        });
        
//        pressedXObservable.switchMap(pressedX -> Observable.combineLatest(drag, source2, combiner))
        
//        pressedXObservable.switchMap(pressedX -> draggedXObservable.map(draggedX -> pressedX).take(1)).subscribe(pressedX -> {
//            vGridLineModel.startDragEvent.onNext(pressedX / panelsView.getWidth());
//        });
        
//        pressedXObservable.switchMap(pressedX -> draggedXObservable.map(draggedX -> pressedX + draggedX - pressedX)).subscribe(deltaX -> {
//            vGridLineModel.dragEvent.onNext(deltaX / panelsView.getWidth());
//        });
    }
}