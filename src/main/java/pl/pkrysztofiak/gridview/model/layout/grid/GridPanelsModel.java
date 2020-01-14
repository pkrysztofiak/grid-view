package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.stream.Stream;

import io.reactivex.Observable;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridPanelsModel {
    
    private final Behaviour behaviour = new Behaviour();
    
    public GridPanelsModel(PanelModel... panels) {
        Stream.of(panels).forEach(behaviour.addPanelRequest::onNext);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return behaviour.getPanels();
    }

    public ObservableList<VGridLineModel> getVGridLines() {
        return behaviour.getVGridLines();
    }

    public Observable<VGridLineModel> vGridLineAdded() {
        return behaviour.vGridLineAdded;
    }
}