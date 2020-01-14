package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.stream.Stream;

import io.reactivex.subjects.PublishSubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Behaviour {

    public final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
 
    private final ObservableList<VGridLineModel> vLines = FXCollections.observableArrayList(); 
    
    {
        addPanelRequest.subscribe(this::onPanelAdded);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMinY()).forEach(x -> {
            Optional<VGridLineModel> optional = findVLine(x);
            if (optional.isPresent()) {
                VGridLineModel vLine = optional.get();
                vLine.add(panel);
            } else {
                
            }
        });
    }
    
    private Optional<VGridLineModel> findVLine(double x) {
        return vLines.stream().filter(vLine -> vLine.getRatioX().equals(x)).findFirst();
    }
}