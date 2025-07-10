// File: src/test/java/com/uptimex/utils/ExcelUtilTest.java
package com.uptimex.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExcelUtilTest {

    @Test
    void testCellNameToIndex() {
        assertArrayEquals(new int[]{0, 0}, ExcelUtil.cellNameToIndex("A1"));
        assertArrayEquals(new int[]{4, 1}, ExcelUtil.cellNameToIndex("B5"));
        assertArrayEquals(new int[]{9, 3}, ExcelUtil.cellNameToIndex("D10"));
        assertArrayEquals(new int[]{0, 25}, ExcelUtil.cellNameToIndex("Z1"));
        assertArrayEquals(new int[]{0, 26}, ExcelUtil.cellNameToIndex("AA1"));
        assertArrayEquals(new int[]{99, 27}, ExcelUtil.cellNameToIndex("AB100"));
    }

    @Test
    void testIndexToCellName() {
        assertEquals("A1", ExcelUtil.indexToCellName(0, 0));
        assertEquals("B5", ExcelUtil.indexToCellName(4, 1));
        assertEquals("D10", ExcelUtil.indexToCellName(9, 3));
        assertEquals("Z1", ExcelUtil.indexToCellName(0, 25));
        assertEquals("AA1", ExcelUtil.indexToCellName(0, 26));
        assertEquals("AB100", ExcelUtil.indexToCellName(99, 27));
    }

    @Test
    void testRoundTrip() {
        // Test that converting to cell name and back yields the same indices
        for (int row = 0; row < 100; row += 10) {
            for (int col = 0; col < 30; col += 5) {
                String cellName = ExcelUtil.indexToCellName(row, col);
                int[] indices = ExcelUtil.cellNameToIndex(cellName);
                assertEquals(row, indices[0]);
                assertEquals(col, indices[1]);
            }
        }
    }
}