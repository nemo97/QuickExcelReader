package com.uptimex.utils;

public class ExcelUtil {

    public static int[] cellNameToIndex(String cellName) {
        int row = 0;
        int col = 0;
        int i = 0;
        // Parse column letters
        while (i < cellName.length() && Character.isLetter(cellName.charAt(i))) {
            col = col * 26 + (Character.toUpperCase(cellName.charAt(i)) - 'A' + 1);
            i++;
        }
        // Parse row number
        String rowStr = cellName.substring(i);
        row = Integer.parseInt(rowStr);

        // Convert to zero-based indices
        return new int[] { row - 1, col - 1 };
    }

    public static String indexToCellName(int row, int col) {
        StringBuilder colName = new StringBuilder();
        int c = col + 1;
        while (c > 0) {
            int rem = (c - 1) % 26;
            colName.insert(0, (char)('A' + rem));
            c = (c - 1) / 26;
        }
        return colName.toString() + (row + 1);
    }
}
