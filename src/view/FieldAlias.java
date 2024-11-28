package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FieldAlias {
    private final StringProperty fieldName;
    private final StringProperty alias;
    private final BooleanProperty selected;

    public FieldAlias(String fieldName) {
        this.fieldName = new SimpleStringProperty(fieldName);
        this.alias = new SimpleStringProperty("");
        this.selected = new SimpleBooleanProperty(false);
    }

    public String getFieldName() {
        return fieldName.get();
    }

    public StringProperty fieldNameProperty() {
        return fieldName;
    }

    public String getAlias() {
        return alias.get();
    }

    public void setAlias(String alias) {
        this.alias.set(alias);
    }

    public StringProperty aliasProperty() {
        return alias;
    }

    public boolean isSelected() {
        return selected.get();
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }
}
