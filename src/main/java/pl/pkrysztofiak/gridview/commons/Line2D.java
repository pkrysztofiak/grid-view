package pl.pkrysztofiak.gridview.commons;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Line2D {

    private final ObjectProperty<Double> startXProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> startYProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> endXProperty = new SimpleObjectProperty<>();
    private final ObjectProperty<Double> endYProperty = new SimpleObjectProperty<>();
    
    public Line2D(double startX, double startY, double endX, double endY) {
        startXProperty.set(startX);
        startYProperty.set(startY);
        endXProperty.set(endX);
        endYProperty.set(endY);
    }
    
    public void setStartX(double x) {
        startXProperty.set(x);
    }
    
    public Double getStartX() {
        return startXProperty.get();
    }
    
    public void setStartY(double y) {
        startYProperty.set(y);
    }
    
    public Double getStartY() {
        return startYProperty.get();
    }
    
    public void setEndX(double x) {
        endXProperty.set(x);
    }
    
    public Double getEndX() {
        return endXProperty.get();
    }
    
    public void setEndY(double y) {
        endYProperty.set(y);
    }
    
    public Double getEndY() {
        return endYProperty.get();
    }
    
    @Override
    public String toString() {
        return "Line[startX=" + startXProperty.get() + ", startY=" + startYProperty.get() + ", endX=" + endXProperty.get() + ", endY=" + endYProperty.get() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((endXProperty.get() == null) ? 0 : endXProperty.get().hashCode());
        result = prime * result + ((endYProperty.get() == null) ? 0 : endYProperty.get().hashCode());
        result = prime * result + ((startXProperty.get() == null) ? 0 : startXProperty.get().hashCode());
        result = prime * result + ((startYProperty.get() == null) ? 0 : startYProperty.get().hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Line2D other = (Line2D) obj;
        if (endXProperty.get() == null) {
            if (other.endXProperty.get() != null) {
                return false;
            }
        } else if (!endXProperty.get().equals(other.endXProperty.get())) {
            return false;
        }
        if (endYProperty.get() == null) {
            if (other.endYProperty.get() != null) {
                return false;
            }
        } else if (!endYProperty.get().equals(other.endYProperty.get())) {
            return false;
        }
        if (startXProperty.get() == null) {
            if (other.startXProperty.get() != null) {
                return false;
            }
        } else if (!startXProperty.get().equals(startXProperty.get())) {
            return false;
        }
        if (startYProperty.get() == null) {
            if (other.startYProperty.get() != null) {
                return false;
            }
        } else if (!startYProperty.get().equals(other.startYProperty.get())) {
            return false;
        }
        return true;
    }
    
    
}