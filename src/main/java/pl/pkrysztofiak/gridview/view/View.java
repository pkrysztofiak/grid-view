package pl.pkrysztofiak.gridview.view;

import javafx.scene.layout.BorderPane;
import pl.pkrysztofiak.gridview.model.Model;
import pl.pkrysztofiak.gridview.view.panels.PanelsView;

public class View extends BorderPane {
    
    public View(Model model) {
        setCenter(new PanelsView(model.getPanels()));
    }
}
