package pl.pkrysztofiak.gridview.view.layout.grid;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import pl.pkrysztofiak.gridview.model.layout.grid.GridPanelsModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.VGridLineModel;
import pl.pkrysztofiak.gridview.view.layout.grid.lines.VGridLineView;

public class GridPanelsView extends Pane {

    private final ObservableList<VGridLineView> vGridLines = FXCollections.observableArrayList();
    public final Observable<Double> widthObservable = JavaFxObservable.valuesOf(widthProperty()).map(Number::doubleValue);
    public final Observable<Double> heightObservable = JavaFxObservable.valuesOf(heightProperty()).map(Number::doubleValue);
    
    public GridPanelsView(GridPanelsModel panelsModel) {
        Bindings.bindContent(getChildren(), vGridLines);

        Observable.fromIterable(panelsModel.getVGridLines()).delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVGridLineModelAdded);
        panelsModel.vGridLineAdded().delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onVGridLineModelAdded);
    }

    private void onVGridLineModelAdded(VGridLineModel vGridLineModel) {
        VGridLineView vGridLine = new VGridLineView(vGridLineModel, this);
        vGridLines.add(vGridLine);
    }
}