package pl.pkrysztofiak.gridview;

import pl.pkrysztofiak.gridview.model.panels.PanelModel;
import pl.pkrysztofiak.gridview.model.panels.HangingProtocol;

public class Layouts {

    public Layouts() {
        //  -----
        //  | | |
        //  -----
        new HangingProtocol(
                new PanelModel(0, 0, 0.5, 1),
                new PanelModel(0.5, 0, 1, 1)
                );
        
        new HangingProtocol(
                new PanelModel(0, 0, 0.5, 0.25),
                new PanelModel(0.5, 0, 1, 0.25), 
                new PanelModel(0, 0.25, 1, 0.75),
                new PanelModel(0, 0.75, 0.5, 1),
                new PanelModel(0.5, 0.75, 1, 1)
                );
        
        new HangingProtocol(
                new PanelModel(0, 0, 0.5, 1),
                new PanelModel(0.5, 0, 1, 1)
                );
    }
}
