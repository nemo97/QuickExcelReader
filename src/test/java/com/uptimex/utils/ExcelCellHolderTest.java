// File: src/test/java/com/uptimex/utils/ExcelCellHolderTest.java
package com.uptimex.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExcelCellHolderTest {

    @Test
    void testConstructorWithIndices() {
        ExcelCellHolder holder = new ExcelCellHolder(2, 3);
        assertEquals(2, holder.getRowIndex());
        assertEquals(3, holder.getColumnIndex());
        assertNotNull(holder.getCellName());
        assertEquals(holder.getCellName(), "D3");
    }

    @Test
    void testConstructorWithCellName() {
        ExcelCellHolder holder = new ExcelCellHolder("B3");
        assertEquals("B3", holder.getCellName());
        assertEquals(2, holder.getRowIndex());
        assertEquals(1, holder.getColumnIndex());
    }

    @Test
    void testSettersAndGetters() {
        ExcelCellHolder holder = new ExcelCellHolder(0, 0);
        holder.setCellName("C5");
        holder.setRowIndex(4);
        holder.setColumnIndex(2);
        assertEquals("C5", holder.getCellName());
        assertEquals(4, holder.getRowIndex());
        assertEquals(2, holder.getColumnIndex());
    }

    @Test
    void testNextRowAndNextCell() {
        ExcelCellHolder holder = new ExcelCellHolder(1, 1);
        holder.nextRow();
        assertEquals(2, holder.getRowIndex());
        holder.nextCell();
        assertEquals(2, holder.getColumnIndex());
        holder.nextRow(3);
        assertEquals(5, holder.getRowIndex());
        holder.nextCell(2);
        assertEquals(4, holder.getColumnIndex());
    }

    @Test
    void testJump() {
        ExcelCellHolder holder = new ExcelCellHolder(0, 0);
        ExcelCellHolder jumped = holder.resetToNewPosition("D10");
        assertEquals("D10", jumped.getCellName());
        assertEquals(9, jumped.getRowIndex());
        assertEquals(3, jumped.getColumnIndex());
    }
}