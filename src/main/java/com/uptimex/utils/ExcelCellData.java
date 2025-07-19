package com.uptimex.utils;

import com.uptimex.config.FieldDefinition;

import java.time.LocalDateTime;
import java.util.Map;

public class ExcelCellData {
    private FieldDefinition fieldDefinition;
    private Object value;

    public FieldDefinition.DataType getCellType() {
        return FieldDefinition.DataType.valueOf(fieldDefinition.getType().toUpperCase());
    }
    public Map<String,?> getTableValue() {
        switch (getCellType()){
            case TABLE:
                return (Map<String, ?>) value; // Assuming value is a Map for TABLE type
            default:
                throw new UnsupportedOperationException("Table type cells cannot be converted to string directly.");

        }
    }
    public String getStringValue() {

        switch (getCellType()){
            case STRING:
                return value != null ? String.valueOf(value) : null;
            case LOCAL_DATE_TIME:
                return value != null ? value.toString() : null; // Assuming value is a Date object
            case LOCAL_DATE:
                return value != null ? value.toString() : null; // Assuming value is a Date object
            case BOOLEAN:
                return value != null ? String.valueOf(value) : null; // Assuming value is a Boolean
            case TABLE:
                throw new UnsupportedOperationException("Table type cells cannot be converted to string directly.");
            default:
                return String.valueOf(value);
        }
    }

    public LocalDateTime getLocalDateTimeValue() {
        switch (getCellType()){
            case LOCAL_DATE_TIME:
                return value != null ? (LocalDateTime)value : null;
            default:
                throw new UnsupportedOperationException("This cell is not of type DOUBLE, cannot be converted to Double directly.");

        }
    }

    public Double getDoubleValue() {
        switch (getCellType()){
            case DOUBLE:
                return value != null ? (Double)value : null;
            default:
                throw new UnsupportedOperationException("This cell is not of type DOUBLE, cannot be converted to Double directly.");

        }
    }
    public Long getLongValue() {
        switch (getCellType()){
            case LONG:
                return value != null ? (Long)value : null;
            default:
                throw new UnsupportedOperationException("This cell is not of type LONG, cannot be converted to Long directly.");

        }
    }

    public FieldDefinition getFieldDefinition() {
        return fieldDefinition;
    }

    public void setFieldDefinition(FieldDefinition fieldDefinition) {
        this.fieldDefinition = fieldDefinition;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExcelCellData{" +
                "fieldDefinition=" + fieldDefinition +
                ", value=" + String.valueOf(value) +
                '}';
    }
}
