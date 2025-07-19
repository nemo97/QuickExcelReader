package com.uptimex.config;



import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class FieldDefinition {
    public static enum DataType {
        STRING("string"), DOUBLE("double"), LONG("long"), LOCAL_DATE_TIME("localDateTime"),LOCAL_DATE("localdate"), BOOLEAN("boolean"), TABLE("table");
        private final String value;
        DataType(String value) {
            this.value = value;
        }
        public String getValue() {
            return value;
        }
    }

    @JsonProperty("name")
    private String name;

    @JsonProperty("xlsColumn")
    private String xlsColumn;

    @JsonProperty("type")
    private String type;

    @JsonProperty("validation")
    private ValidationDefinition validation;

    @JsonProperty("description")
    private String description;

    @JsonProperty("fields")
    private List<FieldDefinition> fields; // for "table" type

    @JsonIgnore
    private DataType dataType;

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getXlsColumn() {
        return xlsColumn;
    }

    public void setXlsColumn(String xlsColumn) {
        this.xlsColumn = xlsColumn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ValidationDefinition getValidation() {
        return validation;
    }

    public void setValidation(ValidationDefinition validation) {
        this.validation = validation;
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

    public DataType getDataType() {
        if(this.dataType == null) {
            this.dataType = DataType.valueOf(type.toUpperCase());
        }
        if(this.dataType == null) {
            throw new IllegalStateException("Invalid data type " + this.type + " for field " + this.name);
        }
        return dataType;
    }
    public void setDataType(DataType dataType) {

        this.dataType = dataType;
    }
    @Override
    public String toString() {
        return "FieldDefinition{" +
                "name='" + name + '\'' +
                ", xlsColumn='" + xlsColumn + '\'' +
                ", type='" + type + '\'' +
                ", validation=" + validation +
                ", description='" + description + '\'' +
                ", fields=" + fields +
                '}';
    }
}
