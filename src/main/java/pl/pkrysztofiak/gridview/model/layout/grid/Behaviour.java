package pl.pkrysztofiak.gridview.model.layout.grid;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.VGridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Behaviour {

    public final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final ObservableList<PanelModel> unmodifiablePanels = FXCollections.unmodifiableObservableList(panels);
 
    private final ObservableList<VGridLineModel> vGridLines = FXCollections.observableArrayList();
    private final ObservableList<VGridLineModel> unmodifiableVGridLines = FXCollections.unmodifiableObservableList(vGridLines);
    public final Observable<VGridLineModel> vGridLineAdded = JavaFxObservable.additionsOf(vGridLines);
    
    {
        //TODO update to Schedulers.single()
        addPanelRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onPanelAdded);
    }
    
    public ObservableList<PanelModel> getPanels() {
        return unmodifiablePanels;
    }
    
    public ObservableList<VGridLineModel> getVGridLines() {
        return unmodifiableVGridLines;
    }
    
    private void onPanelAdded(PanelModel panel) {
        Stream.of(panel.getRatioMinX(), panel.getRatioMaxX()).forEach(ratioX -> {
            Optional<VGridLineModel> optional = findVGridLine(ratioX);
            if (optional.isPresent()) {
                VGridLineModel vGridLine = optional.get();
                vGridLine.add(panel);
            } else {
                VGridLineModel vGridLine = new VGridLineModel(ratioX, vGridLines);
                vGridLine.add(panel);
                vGridLines.add(vGridLine);
            }
        });
    }
    
    private Optional<VGridLineModel> findVGridLine(double x) {
        return vGridLines.stream().filter(vGridLine -> vGridLine.getRatioX().equals(x)).findFirst();
    }
}