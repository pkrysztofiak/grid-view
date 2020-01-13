package pl.pkrysztofiak.gridview.model.hangingprotocol;

import java.util.stream.Stream;

import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.hangingprotocol.panel.HangingProtocolPanel;

public class HangingProtocol {

    private final Behaviour behaviour = new Behaviour();
    
    public HangingProtocol(HangingProtocolPanel... hpPanels) {
        Stream.of(hpPanels).forEach(behaviour.addHpPanelRequest::onNext);
    }
    
    public ObservableList<HangingProtocolPanel> getHpPanels() {
        return behaviour.getHpPanels();
    }
}