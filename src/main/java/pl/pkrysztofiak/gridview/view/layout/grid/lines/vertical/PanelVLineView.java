package pl.pkrysztofiak.gridview.view.layout.grid.lines.vertical;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.PanelVLineModel;

public class PanelVLineView extends Line {

    {
        setStroke(Color.GREEN);
        setStrokeWidth(8.);
    }
    
    public PanelVLineView(PanelVLineModel panelVLineModel) {
        
    }
}