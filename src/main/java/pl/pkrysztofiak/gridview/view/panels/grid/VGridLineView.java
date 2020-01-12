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

//    private final Line testLine = new Line(0, 0, 0, 100);
    
    private final ObservableList<Line> lines = FXCollections.observableArrayList();
    
    {
        setMinWidth(0);
        setPrefWidth(0.);
        setMaxWidth(0);
        
//        testLine.setStroke(Color.RED);
//        testLine.setStrokeWidth(4);
    }
    
    public VGridLineView(VGridLineModel vGridLineModel, PanelsView panelsView) {
//        getChildren().add(testLine);
        Bindings.bindContent(getChildren(), lines);

        vGridLineModel.getLines().forEach(modelLine -> {
            
//            System.out.println(modelLine);
            
            Line line = new Line();
            line.setStroke(Color.RED);
            line.setStrokeWidth(8);
            lines.add(line);
            
            panelsView.heightObservable.subscribe(height -> {
                double viewStartY = height * modelLine.getStartY();
                line.setStartY(viewStartY);
                double viewEndY = height * modelLine.getEndY();
                System.out.println("for line=" + modelLine + " viewStartY=" + viewStartY + " viewEndY=" + viewEndY);
                line.setEndY(viewEndY);
            });
        });
        
        Observable.combineLatest(vGridLineModel.ratioXObservable, panelsView.widthObservable, (ratioX, width) -> ratioX * width).subscribe(x -> {
            setLayoutX(x);
//            vGridLineModel.getLines().forEach(line -> {
//                new Line(0, startY, endX, endY)
//            });
            
//            line.setStartX(x);
//            line.setEndX(x);
        });

        prefHeightProperty().bind(panelsView.heightProperty());
        
//        line.endYProperty().bind(panelsView.heightProperty());
    }
}