package pl.pkrysztofiak.gridview.model.panels.grid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class VGridLineModel {

    private final VGridLinesModel vGridLines;
    
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
    
    private final List<PanelModel> dragPanels = new ArrayList<>();

    private final Map<PanelModel, Line2D> panelToLine = new HashMap<>();
    private final ObservableSet<Line2D> lines = FXCollections.observableSet();
    public final Observable<Line2D> modelLineAddedObservable = JavaFxObservable.additionsOf(lines);
    public final Observable<Line2D> modelLineRemovedObservable = JavaFxObservable.removalsOf(lines);
    
    public final PublishSubject<Double> dragPublishable = PublishSubject.create();
    
    {
        panelAddedObservable.subscribe(this::onPanelAdded);
        panelRemovedObservable.subscribe(this::onPanelRemoved);
        dragPublishable.subscribe(this::onDrag);
    }
    
    public VGridLineModel(double ratioX, VGridLinesModel vGridLines) {
        this.vGridLines = vGridLines;
        ratioXProperty.set(ratioX);
    }
    
    public Double getRatioX() {
        return ratioXProperty.get();
    }
    
    public void add(PanelModel panel) {
        panels.add(panel);
    }
    
    public void addAll(Collection<PanelModel> panels) {
        this.panels.addAll(panels);
    }
    
    private void onPanelRemoved(PanelModel panel) {
        Line2D line = panelToLine.remove(panel);
        lines.remove(line);
    }
    
    private void onPanelAdded(PanelModel panel) {
        if (panel.getRatioMinX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(x -> panel.ratioMinXProperty.set(x));
            
            panel.ratioMinXObservable.filter(panelMinX -> !panelMinX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMinX -> panels.remove(panel));
            
        } else if (panel.getRatioMaxX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(x -> panel.ratioMaxXProperty.set(x));
            panel.ratioMaxXObservable.filter(panelMaxX -> !panelMaxX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(ratioMaxX -> panels.remove(panel));
        } else {
            throw new RuntimeException();
        }
        Line2D line = new Line2D(0, panel.getRatioMinY(), 0, panel.getRatioMaxY());
        panel.ratioMinYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(line::setStartY);
        panel.ratioMaxYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(line::setEndY);
        
        panelToLine.put(panel, line);
        lines.add(line);
    }
    
    public ObservableSet<Line2D> getLines() {
        return lines;
    }
    
    public void startDrag(Point2D ratioStartPoint) {
        dragPanels.clear();
        double ratioY = ratioStartPoint.getY();
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
    
    private void onDrag(double ratioX) {
        if (dragPanels.isEmpty()) {
            throw new RuntimeException();
        }
        if (panels.size() == dragPanels.size()) {
            ratioXProperty.set(ratioX);
        } else {
            List<PanelModel> panelsToRemove = FXCollections.observableArrayList(panels);
            panelsToRemove.removeAll(dragPanels);
            System.out.println("panelsToRemove=" + panelsToRemove);
            panels.removeAll(panelsToRemove);
            VGridLineModel newVGridLine = new VGridLineModel(ratioXProperty.get(), vGridLines);
            newVGridLine.addAll(panelsToRemove);
            vGridLines.vGridLines.add(newVGridLine);
        }
    }
}