package com.uptimex.utils;

public class ExcelCellHolder {

    private String cellName;
    private int rowIndex;
    private int columnIndex;

    public ExcelCellHolder(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.cellName = ExcelUtil.indexToCellName(rowIndex, columnIndex);
    }

    public ExcelCellHolder(String cellValueInitial) {
        this.cellName = cellValueInitial;
        int[] indices = ExcelUtil.cellNameToIndex(cellValueInitial);
        this.rowIndex = indices[0];
        this.columnIndex = indices[1];
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public ExcelCellHolder nextRow() {
        this.rowIndex++;
        return this;
    }
    public ExcelCellHolder nextCell() {
        this.columnIndex++;
        return this;
    }
    public ExcelCellHolder nextCell(int offset) {
        this.columnIndex += offset;
        return this;
    }
    public ExcelCellHolder nextRow(int offset) {
        this.rowIndex += offset;
        return this;
    }

    public ExcelCellHolder resetToNewPosition(String cellName) {
        Assert.notNull(cellName, "Cell name cannot be null");
        int[] indices = ExcelUtil.cellNameToIndex(cellName);
        this.rowIndex = indices[0];
        this.columnIndex = indices[1];
        this.cellName  = cellName;
        return this;
    }

//
//    public static ExcelCellHolder jump(String cellName) {
//        return new ExcelCellHolder(cellName);
//    }
}
