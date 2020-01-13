package pl.pkrysztofiak.gridview.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import pl.pkrysztofiak.gridview.model.hangingprotocol.HP;
import pl.pkrysztofiak.gridview.model.panels.Panel;

public class Model {

    private final ObjectProperty<HP> panelsProperty = new SimpleObjectProperty<>(new HP(
            new Panel(0, 0, 0.5, 0.25),
            new Panel(0.5, 0, 1, 0.25), 
            new Panel(0, 0.25, 1, 0.75),
            new Panel(0, 0.75, 0.5, 1),
            new Panel(0.5, 0.75, 1, 1)
            ));
    
    public HP getPanels() {
        return panelsProperty.get();
    }
}