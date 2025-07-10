package com.uptimex.config;



import com.fasterxml.jackson.annotation.JsonProperty;

public class RangeDefinition {
    @JsonProperty("min")
    private Double min;

    @JsonProperty("max")
    private Double max;

    // getters and setters

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "RangeDefinition{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}