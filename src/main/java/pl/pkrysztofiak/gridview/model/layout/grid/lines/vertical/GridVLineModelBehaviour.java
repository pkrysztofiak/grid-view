package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class GridVLineModelBehaviour {

    private final ObservableList<GridVLineModel> gridVLines;
    
    private final ObjectProperty<Double> ratioXProperty = new SimpleObjectProperty<>();
    public final Observable<Double> ratioXObservable = JavaFxObservable.valuesOf(ratioXProperty);
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
    private final Observable<PanelModel> panelRemovedObservable = JavaFxObservable.removalsOf(panels);
    
    private final SortedList<PanelModel> sortedPanels = new SortedList<>(panels, (panelModel1, panelModel2) -> {
        int result = panelModel1.getRatioMinY().compareTo(panelModel2.getRatioMinY());
        if (result == 0) {
            result = panelModel1.getRatioMinY().compareTo(panelModel2.getRatioMinY());
        }
        return result;
    });

    private final ObservableList<VLineModel> panelsVLines = FXCollections.observableArrayList();
    private final ObservableList<VLineModel> unmodifiablePanelsVLines = FXCollections.unmodifiableObservableList(panelsVLines);
    public final Observable<VLineModel> panelVLineAddedObservable = JavaFxObservable.additionsOf(panelsVLines);
    public final Observable<VLineModel> panelVLineRemovedObservable = JavaFxObservable.removalsOf(panelsVLines);
     
    private final List<PanelModel> dragPanels = new ArrayList<>();
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
    }
    
    public GridVLineModelBehaviour(double ratioX, ObservableList<GridVLineModel> vGridLines, PanelModel... panels) {
        ratioXProperty.set(ratioX);
        this.gridVLines = vGridLines;
        Stream.of(panels).forEach(this::onAddPanelRequest);
    }
    
    public GridVLineModelBehaviour(double ratioX, ObservableList<GridVLineModel> vGridLines, Collection<PanelModel> panels) {
        ratioXProperty.set(ratioX);
        this.gridVLines = vGridLines;
        panels.stream().forEach(this::onAddPanelRequest);
    }
    
    public ObservableList<VLineModel> getPanelsVLines() {
        return unmodifiablePanelsVLines;
    }
    
    public Double getRatioX() {
        return ratioXProperty.get();
    }

    public void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        //TODO przepisać w wytwrzanie w vLine
        if (panel.getRatioMinX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panel::setRatioMinX);
            panel.ratioMinXObservable.filter(panelMinX -> !panelMinX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMinX -> panels.remove(panel));
        } else if (panel.getRatioMaxX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panel::setRatioMaxX);
            panel.ratioMaxXObservable.filter(panelMaxX -> !panelMaxX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMinX -> panels.remove(panel));
        } else {
            throw new RuntimeException();
        }
//        Observable.merge(panel.ratioMinXObservable, panel.ratioMaxXObservable).filter(panelRatioX -> !panelRatioX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panelRatioX -> panels.remove(panel));
        
        Line2D vLine = new Line2D(0, panel.getRatioMinY(), 0, panel.getRatioMaxY());
        panel.ratioMinYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(vLine::setStartY);
        panel.ratioMaxYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(vLine::setEndY);
        
        VLineModel panelVLine = createPanelVLineModel(panel);
        panelsVLines.add(panelVLine);
        panelRemovedObservable.filter(panel::equals).subscribe(panelRemoved -> panelsVLines.remove(panelVLine));
    }
    
    private VLineModel createPanelVLineModel(PanelModel panel) {
        VLineModel panelVLine = new VLineModel();
        panel.ratioMinYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panelVLine::setRationMinY);
        panel.ratioMaxYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panelVLine::setRationMaxY);
        return panelVLine;
    }
    
    void onStartDrag(Point2D point) {
        dragPanels.clear();
        double ratioY = point.getY();
        sortedPanels.stream().filter(panel -> ratioY >= panel.getRatioMinY() && ratioY <= panel.getRatioMaxY()).findFirst().ifPresent(selectedPanel -> {
            int index = sortedPanels.indexOf(selectedPanel);
            dragPanels.add(selectedPanel);
            for (int i = index, j = i + 1; j < sortedPanels.size(); i++, j++) {
                PanelModel panel1 = sortedPanels.get(i);
                PanelModel panel2 = sortedPanels.get(j);
                if (panel1.isVerticallyConnected(panel2)) {
                    dragPanels.add(panel2);
                } else {
                    break;
                }
            }
            for (int i = index, j = i - 1; j >= 0; i--, j--) {
                PanelModel panel1 = sortedPanels.get(i);
                PanelModel panel2 = sortedPanels.get(j);
                if (panel1.isVerticallyConnected(panel2)) {
                    dragPanels.add(panel2);
                } else {
                    break;
                }
            }
        });
    }
    
    void onDrag(double ratioX) {
        if (dragPanels.isEmpty()) {
            throw new RuntimeException();
        }
        if (panels.size() == dragPanels.size()) {
            ratioXProperty.set(ratioX);
        } else {
            List<PanelModel> panelsToRemove = FXCollections.observableArrayList(panels);
            panelsToRemove.removeAll(dragPanels);
            panels.removeAll(panelsToRemove);
            gridVLines.add(new GridVLineModel(ratioXProperty.get(), gridVLines, panelsToRemove));
        }
    }
}