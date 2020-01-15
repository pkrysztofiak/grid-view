package pl.pkrysztofiak.gridview.view.layout.grid.lines.horizontal;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.HLineModel;

public class HLineView extends Line {

    {
        setStroke(Color.RED);
        setStrokeWidth(8.);
    }
    
    public HLineView(HLineModel hLineModel) {
        System.out.println("create HLineView kurwa!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
