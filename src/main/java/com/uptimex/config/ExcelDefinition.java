package com.uptimex.config;



import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ExcelDefinition {
    @JsonProperty("schemaVersion")
    private String schemaVersion;

    @JsonProperty("sheets")
    private List<SheetDefinition> sheets;

    // getters and setters


    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public List<SheetDefinition> getSheets() {
        return sheets;
    }

    public void setSheets(List<SheetDefinition> sheets) {
        this.sheets = sheets;
    }

    @Override
    public String toString() {
        return "ExcelDefinition{" +
                "schemaVersion='" + schemaVersion + '\'' +
                ", sheets=" + sheets +
                '}';
    }
}