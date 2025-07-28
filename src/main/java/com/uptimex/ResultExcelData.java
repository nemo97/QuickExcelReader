package com.uptimex;

import com.uptimex.utils.ExcelCellData;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class ResultExcelData {

    private Map<String,Map<String, ExcelCellData>> _data;

    public ResultExcelData(Map<String, Map<String, ExcelCellData>> _data) {
        this._data = _data;
    }

    public Map<String, Map<String, ExcelCellData>> getData() {
        return _data;
    }
    public void setData(Map<String, Map<String, ExcelCellData>> data) {
        this._data = data;
    }
    public void addData(String sheetName,Map<String, ExcelCellData> data) {
        this._data.put(sheetName,data);
    }

    public Map<String, Map<String, Object>> getDataMap() {
        if (_data == null) {
            return null;
        }
        Map<String, Map<String, Object>> result = _data.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().entrySet().stream()
                                .collect(java.util.stream.Collectors.toMap(
                                        Map.Entry::getKey,
                                        e -> {
                                            ExcelCellData cellData = e.getValue();
                                            switch (cellData.getCellType()){
                                                case TABLE:
                                                    List<Map<String,ExcelCellData>> tableData = cellData.getTableValue();

                                                    List<Map<String,Object>> resultTableData =  tableData.stream()
                                                            .map(row -> row.entrySet().stream()
                                                                    .collect(java.util.stream.Collectors.toMap(
                                                                            Map.Entry::getKey,
                                                                            rowEntry -> rowEntry.getValue().getValue())))
                                                            .collect(Collectors.toList());

                                                    return resultTableData; // Convert table data to a list of maps
                                                default:
                                                    return cellData.getValue(); // Return the value directly for other types
                                            }
                                        }
                                ))
                ));
        return result;
//        for (Map.Entry<String, Map<String, ExcelCellData>> sheetEntry : result.entrySet()) {
//            System.out.println("Sheet: " + sheetEntry.getKey());
//
//            for (Map.Entry<String, ExcelCellData> fieldEntry : sheetEntry.getValue().entrySet()) {
//                //System.out.println("  Field: " + fieldEntry.getKey() + " -> " + fieldEntry.getValue().getValue());
//                ExcelCellData cellData = fieldEntry.getValue();
//                switch (cellData.getCellType()){
//                    case TABLE:
//                        System.out.println("  table: Field: " + fieldEntry.getKey());
//                        List<Map<String,ExcelCellData>> tableData = cellData.getTableValue();
//                        for (Map<String, ExcelCellData> row : tableData) {
//                            System.out.println("    Row: ");
//                            for (Map.Entry<String, ExcelCellData> rowEntry : row.entrySet()) {
//                                //System.out.println("      " + rowEntry.getKey() + " -> " + rowEntry.getValue().getStringValue());
//                                ExcelCellData cellData2 = rowEntry.getValue();
//                                switch (cellData2.getCellType()) {
//                                    case STRING:
//                                        System.out.println("  String: Field: " + rowEntry.getKey() + " -> " + cellData2.getStringValue());
//                                        break;
//                                    case LONG:
//                                        System.out.println("  Long: Field: " + rowEntry.getKey() + " -> " + cellData2.getLongValue());
//                                        break;
//                                    case BOOLEAN:
//                                        System.out.println("  Boolean : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
//                                        break;
//                                    case LOCAL_DATE_TIME:
//                                        System.out.println("  LocalDateTime : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
//                                        break;
//                                    default:
//                                        System.out.println("  Field: " + rowEntry.getKey() + " -> Unknown type");
//                                }
//                            }
//                        }
//                        break;
//                    case STRING:
//                        System.out.println("  String: Field: " + fieldEntry.getKey() + " -> " + cellData.getStringValue());
//                        break;
//                    case LONG:
//                        System.out.println("  Long: Field: " + fieldEntry.getKey() + " -> " + cellData.getLongValue());
//                        break;
//                    case BOOLEAN:
//                        System.out.println("  Boolean : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
//                        break;
//                    case LOCAL_DATE_TIME:
//                        System.out.println("  LocalDateTime : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
//                        break;
//                    default:
//                        System.out.println("  Field: " + fieldEntry.getKey() + " -> Unknown type");
//                }
//            }
//        }

//        return _data;
    }

//    public T getDataObject() {
//        return _data;
//    }

    @Override
    public String toString() {
        return "ResultExcelData{" +
                "_data=" + getDataMap() +
                '}';
    }

}
