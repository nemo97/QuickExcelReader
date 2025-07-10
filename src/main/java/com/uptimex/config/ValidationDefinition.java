package com.uptimex.config;



import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidationDefinition {
    @JsonProperty("required")
    private Boolean required;

    @JsonProperty("maxLength")
    private Integer maxLength;

    @JsonProperty("regex")
    private String regex;

    @JsonProperty("range")
    private RangeDefinition range;

    // getters and setters

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public RangeDefinition getRange() {
        return range;
    }

    public void setRange(RangeDefinition range) {
        this.range = range;
    }

    @Override
    public String toString() {
        return "ValidationDefinition{" +
                "required=" + required +
                ", maxLength=" + maxLength +
                ", regex='" + regex + '\'' +
                ", range=" + range +
                '}';
    }
}
