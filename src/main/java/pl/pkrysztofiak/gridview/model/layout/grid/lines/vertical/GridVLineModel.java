package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridVLineModel {

    public final PublishSubject<PanelModel> addPanel = PublishSubject.create();
    public final PublishSubject<Point2D> startDrag = PublishSubject.create();
    public final PublishSubject<Double> drag = PublishSubject.create();
    
    private final ObservableList<GridVLineModel> gridVLines;
    
    private final ObjectProperty<Double> ratioXProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioXObservable = JavaFxObservable.valuesOf(ratioXProperty);
    
    private final ObservableList<VLineModel> vLines = FXCollections.observableArrayList();
    private final ObservableList<VLineModel> unmodifiableVLines = FXCollections.unmodifiableObservableList(vLines);
    private final ObservableList<VLineModel> sortedVLines = new SortedList<>(unmodifiableVLines, (vLine1, vLine2) -> vLine1.compareTo(vLine2));
    public final Observable<VLineModel> vLineAddedObservable = JavaFxObservable.additionsOf(vLines);
    public final Observable<VLineModel> vLineRemovedObservable = JavaFxObservable.removalsOf(vLines);
    
    private final List<VLineModel> dragVLines = new ArrayList<>();
    
    {
        addPanel.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::addPanel);
        startDrag.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::startDrag);
        drag.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::drag);
    }
    
    public GridVLineModel(double ratioX, ObservableList<GridVLineModel> gridVLines, PanelModel... panels) {
        this(ratioX, gridVLines, Arrays.asList(panels));
    }
    
    public GridVLineModel(double ratioX, ObservableList<GridVLineModel> gridVLines, List<PanelModel> panels) {
        ratioXProperty.set(ratioX);
        this.gridVLines = gridVLines;
        panels.stream().forEach(this::addPanel);
    }
    
    public ObservableList<VLineModel> getVLines() {
        return unmodifiableVLines;
    }
    
    public Double getRatioX() {
        return ratioXProperty.get();
    }

    private void addPanel(PanelModel panel) {
        VLineModel vLine = createVLine(panel);
        vLines.add(vLine);
    }
    
    private VLineModel createVLine(PanelModel panel) {
        System.out.println("createVLine");
        VLineModel vLine = new VLineModel(panel);
        if (panel.getRatioMinX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(panel::setRatioMinX);
            panel.ratioMinXObservable.filter(ratioMinX -> !ratioMinX.equals(ratioXProperty.get())).takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(ratioMinX -> vLines.remove(vLine));
        } else if (panel.getRatioMaxX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(panel::setRatioMaxX);
            panel.ratioMaxXObservable.filter(ratioMinX -> !ratioMinX.equals(ratioXProperty.get())).takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(ratioMaxX -> vLines.remove(vLine));
        }
        panel.ratioMinYObservable.takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(vLine::setRatioMinY);
        panel.ratioMaxYObservable.takeUntil(vLineRemovedObservable.filter(vLine::equals)).subscribe(vLine::setRationMaxY);
        return vLine;
    }
    
    private void startDrag(Point2D point) {
        dragVLines.clear();
        double ratioY = point.getY();
        sortedVLines.stream().filter(vLine -> vLine.contains(ratioY)).findFirst().ifPresent(vLine -> {
            dragVLines.add(vLine);
            int index = sortedVLines.indexOf(vLine);
            for (int i = index, j = i + 1; j < sortedVLines.size(); i++, j++) {
                VLineModel panel1 = sortedVLines.get(i);
                VLineModel panel2 = sortedVLines.get(j);
                if (panel1.isConnected(panel2)) {
                    dragVLines.add(panel2);
                } else {
                    break;
                }
            }
            for (int i = index, j = i - 1; j >= 0; i--, j--) {
                VLineModel panel1 = sortedVLines.get(i);
                VLineModel panel2 = sortedVLines.get(j);
                if (panel1.isConnected(panel2)) {
                    dragVLines.add(panel2);
                } else {
                    break;
                }
            }
        });
    }
    
    private void drag(double ratioX) {
        if (dragVLines.isEmpty()) {
            throw new RuntimeException();
        }
        if (vLines.size() != dragVLines.size()) {
            List<VLineModel> vLinesToRemove = new ArrayList<>(vLines);
            vLinesToRemove.removeAll(dragVLines);
            vLines.removeAll(vLinesToRemove);
            gridVLines.add(new GridVLineModel(ratioXProperty.get(), gridVLines, vLinesToRemove.stream().map(VLineModel::getPanel).collect(Collectors.toList())));
        }
        ratioXProperty.set(ratioX);
    }
}