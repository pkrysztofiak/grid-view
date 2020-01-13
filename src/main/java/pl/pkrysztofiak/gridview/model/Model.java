package pl.pkrysztofiak.gridview.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.pkrysztofiak.gridview.model.layout.GridPanelsModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Model {

    private final ObjectProperty<GridPanelsModel> panelsProperty = new SimpleObjectProperty<>(new GridPanelsModel(
            new PanelModel(0, 0, 0.5, 0.25),
            new PanelModel(0.5, 0, 1, 0.25), 
            new PanelModel(0, 0.25, 1, 0.75),
            new PanelModel(0, 0.75, 0.5, 1),
            new PanelModel(0.5, 0.75, 1, 1)
            ));

    {
        //  -----
        //  | | |
        //  -----
        new GridPanelsModel(
                new PanelModel(0, 0, 0.5, 1),
                new PanelModel(0.5, 0, 1, 1)
                );
        
        new GridPanelsModel(
                new PanelModel(0, 0, 0.5, 0.25),
                new PanelModel(0.5, 0, 1, 0.25), 
                new PanelModel(0, 0.25, 1, 0.75),
                new PanelModel(0, 0.75, 0.5, 1),
                new PanelModel(0.5, 0.75, 1, 1)
                );
        
        new GridPanelsModel(
                new PanelModel(0, 0, 0.5, 1),
                new PanelModel(0.5, 0, 1, 1)
                );
    }
    
    public GridPanelsModel getPanels() {
        return panelsProperty.get();
    }
}