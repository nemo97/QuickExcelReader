package com.uptimex.demo.dtos;

import java.util.ArrayList;
import java.util.List;

public class SampleExcelData {
    List<SampleExcelSheetData> sheets = new ArrayList<>();

    public List<SampleExcelSheetData> getSheets() {
        return sheets;
    }

    public void setSheets(List<SampleExcelSheetData> sheets) {
        this.sheets = sheets;
    }
}
