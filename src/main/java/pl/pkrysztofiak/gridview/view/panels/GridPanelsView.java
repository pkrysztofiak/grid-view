package pl.pkrysztofiak.gridview.view.panels;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import pl.pkrysztofiak.gridview.model.panels.GridPanelsModel;
import pl.pkrysztofiak.gridview.model.panels.grid.VGridLineModel;
import pl.pkrysztofiak.gridview.view.panels.grid.VGridLineView;

public class GridPanelsView extends Pane {

    private final ObservableList<VGridLineView> vGridLines = FXCollections.observableArrayList();
    public final Observable<Double> widthObservable = JavaFxObservable.valuesOf(widthProperty()).map(Number::doubleValue);
    public final Observable<Double> heightObservable = JavaFxObservable.valuesOf(heightProperty()).map(Number::doubleValue);
    
    public GridPanelsView(GridPanelsModel panelsModel) {
        Bindings.bindContent(getChildren(), vGridLines);

        Observable.fromIterable(panelsModel.vGridLines.vGridLines).delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVGridLineModelAdded);
        panelsModel.vGridLines.vGridLineAddedObservable.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVGridLineModelAdded);
    }

    private void onVGridLineModelAdded(VGridLineModel vGridLineModel) {
        VGridLineView vGridLine = new VGridLineView(vGridLineModel, this);
        vGridLines.add(vGridLine);
    }
}