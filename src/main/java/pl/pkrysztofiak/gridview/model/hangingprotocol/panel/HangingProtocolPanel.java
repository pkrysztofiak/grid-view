package pl.pkrysztofiak.gridview.model.hangingprotocol.panel;

import java.util.stream.Stream;

import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class HangingProtocolPanel {

    private final Behaviour behaviour = new Behaviour();

    public HangingProtocolPanel(PanelModel... panels) {
        Stream.of(panels).forEach(behaviour.addPanelRequestPublishable::onNext);
    }
    
    public void addPanel(PanelModel panel) {
        behaviour.addPanelRequestPublishable.onNext(panel);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return behaviour.getPanels();
    }
}