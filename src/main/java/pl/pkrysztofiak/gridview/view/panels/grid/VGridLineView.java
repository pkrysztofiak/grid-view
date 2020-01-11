package pl.pkrysztofiak.gridview.view.panels.grid;

import io.reactivex.Observable;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.view.panels.PanelsView;

public class VGridLineView extends Group {

    private final Line line = new Line(0, 0, 0, 0);
    
    {
        line.setStroke(Color.RED);
        line.setStrokeWidth(4);
    }
    
    public VGridLineView(VGridLineModel vGridLineModel, PanelsView panelsView) {
        getChildren().add(line);

        Observable.combineLatest(vGridLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width).subscribe(x -> {
            line.setStartX(x);
            line.setEndX(x);
        });

        line.endYProperty().bind(panelsView.heightProperty());
    }
}