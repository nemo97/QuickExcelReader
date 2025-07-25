package com.uptimex.demo.dtos;

import java.util.ArrayList;
import java.util.List;

public class SampleExcelSheetData {
    private String field1;
    private Long field2;
    private boolean field3;

    private List<SampleExcelSheetTableData> field4 = new ArrayList<>();

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public Long getField2() {
        return field2;
    }

    public void setField2(Long field2) {
        this.field2 = field2;
    }

    public boolean isField3() {
        return field3;
    }

    public void setField3(boolean field3) {
        this.field3 = field3;
    }

    public List<SampleExcelSheetTableData> getField4() {
        return field4;
    }

    public void setField4(List<SampleExcelSheetTableData> field4) {
        this.field4 = field4;
    }
}
