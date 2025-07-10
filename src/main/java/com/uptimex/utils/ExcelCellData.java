package com.uptimex.utils;

import com.uptimex.config.FieldDefinition;

public class ExcelCellData {
    private FieldDefinition fieldDefinition;
    private Object value;

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
