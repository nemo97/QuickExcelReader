package com.uptimex.utils;

import com.uptimex.config.FieldDefinition;
import org.apache.poi.util.Internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Internal
public class ExcelDataInner {
    private Map<String, ExcelCellData> _data = new HashMap<>();
//    private String fieldName;
//    private String fieldType;
//    private String description;

    public ExcelDataInner() {
    }

    public void put(String cellName, FieldDefinition fieldDefinition, Object value) {
        Objects.requireNonNull(cellName, "cellName cannot be null");
        Objects.requireNonNull(fieldDefinition, "fieldDefinition cannot be null");
        Objects.requireNonNull(value, "value cannot be null");

        ExcelCellData cellData = new ExcelCellData();
        cellData.setFieldDefinition(fieldDefinition);
        cellData.setValue(value);

        _data.put(cellName, cellData);
    }

    public ExcelCellData get(String cellName) {
        Objects.requireNonNull(cellName, "cellName cannot be null");
        return _data.get(cellName);
    }

//
//    public void setFieldName(String fieldName) {
//        this.fieldName = fieldName;
//    }
//
//    public String getFieldName() {
//        return fieldName;
//    }
//
//    public void setFieldType(String fieldType) {
//        this.fieldType = fieldType;
//    }
//
//    public String getFieldType() {
//        return fieldType;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getDescription() {
//        return description;
//    }

    @Override
    public String toString() {
        return "ExcelDataInner{" +
                "_data=" + _data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExcelDataInner)) return false;
        ExcelDataInner that = (ExcelDataInner) o;
        return Objects.equals(_data, that._data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_data);
    }
}

