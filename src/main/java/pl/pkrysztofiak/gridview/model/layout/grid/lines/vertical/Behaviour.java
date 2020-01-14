package pl.pkrysztofiak.gridview.model.layout.grid.lines.vertical;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Point2D;
import pl.pkrysztofiak.gridview.commons.Line2D;
import pl.pkrysztofiak.gridview.model.panels.PanelModel;

public class Behaviour {

    public final PublishSubject<PanelModel> addPanelRequest = PublishSubject.create();
    public final PublishSubject<Point2D> startDragRequest = PublishSubject.create();
    public final PublishSubject<Double> dragRequest = PublishSubject.create();
    
    private final ObservableList<VGridLineModel> vGridLines;
    
    private final ReadOnlyObjectWrapper<Double> ratioXProperty = new ReadOnlyObjectWrapper<>();
    public final Observable<Double> ratioXObservable = JavaFxObservable.valuesOf(ratioXProperty);
    
    private final ObservableList<PanelModel> panels = FXCollections.observableArrayList();
    private final Observable<PanelModel> panelAddedObservable = JavaFxObservable.additionsOf(panels);
    private final Observable<PanelModel> panelRemovedObservable = JavaFxObservable.additionsOf(panels);
    
    private final SortedList<PanelModel> sortedPanels = new SortedList<>(panels, (panelModel1, panelModel2) -> {
        int result = panelModel1.getRatioMinY().compareTo(panelModel2.getRatioMinY());
        if (result == 0) {
            result = panelModel1.getRatioMinY().compareTo(panelModel2.getRatioMinY());
        }
        return result;
    });

    private final ObservableSet<Line2D> vLines = FXCollections.observableSet();
    private final ObservableSet<Line2D> unmodifiableVLines = FXCollections.unmodifiableObservableSet(vLines);
    public final Observable<Line2D> vLineAddedObservable = JavaFxObservable.additionsOf(vLines);
    public final Observable<Line2D> vLineRemovedObservable = JavaFxObservable.removalsOf(vLines);
    
    private final List<PanelModel> dragPanels = new ArrayList<>();
    
    {
        //TODO przepisać to single
        addPanelRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onAddPanelRequest);
        panelAddedObservable.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onPanelAdded);
        startDragRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onStartDrag);
        dragRequest.delay(0, TimeUnit.SECONDS, JavaFxScheduler.platform()).subscribe(this::onDrag);
    }
    
    public Behaviour(double ratioX, ObservableList<VGridLineModel> vGridLines, PanelModel... panels) {
        ratioXProperty.set(ratioX);
        this.vGridLines = vGridLines;
        Stream.of(panels).forEach(addPanelRequest::onNext);
    }
    
    public Behaviour(double ratioX, ObservableList<VGridLineModel> vGridLines, Collection<PanelModel> panels) {
        ratioXProperty.set(ratioX);
        this.vGridLines = vGridLines;
        panels.stream().forEach(addPanelRequest::onNext);
    }
    
    public ObservableSet<Line2D> getVLines() {
        return unmodifiableVLines;
    }
    
    public Double getRatioX() {
        return ratioXProperty.get();
    }

    private void onAddPanelRequest(PanelModel panel) {
        panels.add(panel);
    }
    
    private void onPanelAdded(PanelModel panel) {
        if (panel.getRatioMinX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panel::setRatioMinX);
        } else if (panel.getRatioMaxX().equals(ratioXProperty.get())) {
            ratioXObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panel::setRatioMaxX);
        } else {
            throw new RuntimeException();
        }
        Observable.merge(panel.ratioMinXObservable, panel.ratioMaxXObservable).filter(panelRatioX -> !panelRatioX.equals(ratioXProperty.get())).takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(panelRatioX -> panels.remove(panel));
        
        Line2D vLine = new Line2D(0, panel.getRatioMinY(), 0, panel.getRatioMaxY());
        panel.ratioMinYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(vLine::setStartY);
        panel.ratioMaxYObservable.takeUntil(panelRemovedObservable.filter(panel::equals)).subscribe(vLine::setEndY);
        
        vLines.add(vLine);
    }
    
    private void onStartDrag(Point2D point) {
        System.out.println("onStartDrag");
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
    
    private void onDrag(double ratioX) {
        System.out.println("onDrag()");
        System.out.println("dragPanels.size=" + dragPanels.size());
        if (dragPanels.isEmpty()) {
            throw new RuntimeException();
        }
        if (panels.size() == dragPanels.size()) {
            ratioXProperty.set(ratioX);
        } else {
            List<PanelModel> panelsToRemove = FXCollections.observableArrayList(panels);
            panelsToRemove.removeAll(dragPanels);
            panels.removeAll(panelsToRemove);
            vGridLines.add(new VGridLineModel(ratioXProperty.get(), vGridLines, panelsToRemove));
//            VGridLineModel newVGridLine = new VGridLineModel(ratioXProperty.get(), null);
//          newVGridLine.addAll(panelsToRemove);
//          vGridLines.add(newVGridLine);
        }
    }
}