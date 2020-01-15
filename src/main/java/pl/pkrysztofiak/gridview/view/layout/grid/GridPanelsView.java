package pl.pkrysztofiak.gridview.view.layout.grid;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import pl.pkrysztofiak.gridview.model.layout.grid.GridPanelsModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal.GridHLineModel;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical.GridVLineModel;
import pl.pkrysztofiak.gridview.view.layout.grid.lines.horizontal.GridHLineView;
import pl.pkrysztofiak.gridview.view.layout.grid.lines.vertical.GridVLineView;

public class GridPanelsView extends Pane {

    private final ObservableList<GridVLineView> gridVLines = FXCollections.observableArrayList();
    private final ObservableList<GridHLineView> gridHLines = FXCollections.observableArrayList();
    public final Observable<Double> widthObservable = JavaFxObservable.valuesOf(widthProperty()).map(Number::doubleValue);
    public final Observable<Double> heightObservable = JavaFxObservable.valuesOf(heightProperty()).map(Number::doubleValue);
    
    public GridPanelsView(GridPanelsModel gridPanelsModel) {
        //TODO to jest dobrze napisać klase nadrzędną do GridVLineView i GridHLineView
//        Bindings.bindContent(getChildren(), gridVLines);

        Observable.fromIterable(gridPanelsModel.getGridVLines()).delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onGridVLineModelAdded);
        gridPanelsModel.gridVLineAddedObservable().delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onGridVLineModelAdded);
        
        Observable.fromIterable(gridPanelsModel.getGridHLines()).delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onGridHLineModelAdded);
        gridPanelsModel.gridHLineAddedObservable().delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onGridHLineModelAdded);
    }

    private void onGridVLineModelAdded(GridVLineModel vGridLineModel) {
        GridVLineView vGridLine = new GridVLineView(vGridLineModel, this);
        gridVLines.add(vGridLine);
        getChildren().add(vGridLine);
    }
    
    private void onGridHLineModelAdded(GridHLineModel gridHLineModel) {
        GridHLineView gridHLine = new GridHLineView(gridHLineModel, this);
        gridHLines.add(gridHLine);
        getChildren().add(gridHLine);
    }
}