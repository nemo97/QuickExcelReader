package com.uptimex.config;



import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class SheetDefinition {
    @JsonProperty("sheetName")
    private String sheetName;

    @JsonProperty("sheetIndex")
    private int sheetIndex;

    @JsonProperty("description")
    private String description;

    @JsonProperty("fields")
    private List<FieldDefinition> fields;

    // getters and setters


    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
   }
    @Override
    public String toString() {
        return "SheetDefinition{" +
                "sheetName='" + sheetName + '\'' +
                ", sheetIndex=" + sheetIndex +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                '}';
    }
}