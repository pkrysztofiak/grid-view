package pl.pkrysztofiak.gridview.view.panels.grid;

import io.reactivex.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.view.panels.PanelsView;

public class VGridLineView extends Pane {

    private final ObservableList<Line> lines = FXCollections.observableArrayList();
    
    {
        setMinWidth(0);
        setPrefWidth(0.);
        setMaxWidth(0);
    }
    
    public VGridLineView(VGridLineModel vGridLineModel, PanelsView panelsView) {
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
    }
}