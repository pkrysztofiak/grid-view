package pl.pkrysztofiak.gridview.model.layout.grid.lines.horizontal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.model.layout.grid.lines.GridLineModel;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridHLineModel extends GridLineModel {

    private final ObservableList<GridHLineModel> gridHLines;
    
    private final ObjectProperty<Double> ratioYProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioYObservable = JavaFxObservable.valuesOf(ratioYProperty);
    
    private final ObservableList<HLineModel> hLines = FXCollections.observableArrayList();
    private final ObservableList<HLineModel> unmodifiableHLines = FXCollections.unmodifiableObservableList(hLines);
    private final ObservableList<HLineModel> sortedHLines = new SortedList<>(unmodifiableHLines, (hLine1, hLine2) -> hLine1.compareTo(hLine2));
    public final Observable<HLineModel> hLineAddedObservable = JavaFxObservable.additionsOf(hLines);
    public final Observable<HLineModel> hLineRemovedObservable = JavaFxObservable.removalsOf(hLines);
    
    private final List<HLineModel> dragHLines = new ArrayList<>();
    
    public GridHLineModel(double ratioY, ObservableList<GridHLineModel> gridHLines, PanelModel... panels) {
        this(ratioY, gridHLines, Arrays.asList(panels));
    }
    
    public GridHLineModel(double ratioY, ObservableList<GridHLineModel> gridHLines, List<PanelModel> panels) {
        ratioYProperty.set(ratioY);
        this.gridHLines = gridHLines;
        panels.stream().forEach(this::addPanel);
    }
    
    public ObservableList<HLineModel> getHLines() {
        return unmodifiableHLines;
    }
    
    public Double getRatioY() {
        return ratioYProperty.get();
    }
    
    @Override
    protected void addPanel(PanelModel panel) {
        HLineModel hLine = createHLine(panel);
        System.out.println("panel=" + panel);
        System.out.println("hLineModel=" + hLine);
        hLines.add(hLine);
    }
    
    private HLineModel createHLine(PanelModel panel) {
        System.out.println("createHLine");
        HLineModel hLine = new HLineModel(panel);
        Observable<HLineModel> hLineRemoved = hLineRemovedObservable.filter(hLine::equals);
        
        if (panel.getRatioMinY().equals(ratioYProperty.get())) {
            ratioYObservable.takeUntil(hLineRemoved).subscribe(panel::setRatioMinY);
            panel.ratioMinYObservable.filter(ratioMinY -> !ratioMinY.equals(ratioYProperty.get())).takeUntil(hLineRemoved).subscribe(ratioMinX -> hLines.remove(hLine));
        } else if (panel.getRatioMaxY().equals(ratioYProperty.get())) {
            ratioYObservable.takeUntil(hLineRemoved).subscribe(panel::setRatioMaxY);
            panel.ratioMaxYObservable.filter(ratioMaxY -> !ratioMaxY.equals(ratioYProperty.get())).takeUntil(hLineRemoved).subscribe(ratioMaxY -> hLines.remove(hLine));
        }
        panel.ratioMinXObservable.takeUntil(hLineRemoved).subscribe(hLine::setRatioMinX);
        panel.ratioMaxXObservable.takeUntil(hLineRemoved).subscribe(hLine::setRationMaxX);
        return hLine;
    }

    @Override
    protected void startDrag(Point2D point) {
        dragHLines.clear();
        double ratioY = point.getY();
        sortedHLines.stream().filter(hLine -> hLine.contains(ratioY)).findFirst().ifPresent(hLine -> {
            dragHLines.add(hLine);
            int index = sortedHLines.indexOf(hLine);
            for (int i = index, j = i + 1; j < sortedHLines.size(); i++, j++) {
                HLineModel panel1 = sortedHLines.get(i);
                HLineModel panel2 = sortedHLines.get(j);
                if (panel1.isConnected(panel2)) {
                    dragHLines.add(panel2);
                } else {
                    break;
                }
            }
            for (int i = index, j = i - 1; j >= 0; i--, j--) {
                HLineModel panel1 = sortedHLines.get(i);
                HLineModel panel2 = sortedHLines.get(j);
                if (panel1.isConnected(panel2)) {
                    dragHLines.add(panel2);
                } else {
                    break;
                }
            }
        });
    }

    @Override
    protected void drag(double ratioX) {
        if (dragHLines.isEmpty()) {
            throw new RuntimeException();
        }
        if (hLines.size() != dragHLines.size()) {
            List<HLineModel> hLinesToRemove = new ArrayList<>(hLines);
            hLinesToRemove.removeAll(dragHLines);
            hLines.removeAll(hLinesToRemove);
            gridHLines.add(new GridHLineModel(ratioYProperty.get(), gridHLines, hLinesToRemove.stream().map(HLineModel::getPanel).collect(Collectors.toList())));
        }
        ratioYProperty.set(ratioX);
    }
    
}