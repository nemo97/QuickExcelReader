package com.uptimex;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class QuickExcelReaderTest {

    @Test
    void testReadReturnsMap() throws Exception {
        // Copy Excel and JSON files from classpath to temp files
        InputStream excelStream = getClass().getResourceAsStream("/files/simple_excle.xlsx");
        Path excelFile = Files.createTempFile("test", ".xlsx");
        Files.copy(excelStream, excelFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        InputStream jsonStream = getClass().getResourceAsStream("/def1.json");
        Path jsonFile = Files.createTempFile("test", ".json");
        Files.copy(jsonStream, jsonFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);


        QuickExcelReader reader = QuickExcelReader.builder()
                .excelPath(excelFile)
                .jsonPath(jsonFile)
                .build();

        ResultExcelData result = reader.read();

        System.out.println("Result: " + result);

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


        assertNotNull(result);

        Files.deleteIfExists(excelFile);
        Files.deleteIfExists(jsonFile);
    }

    @Test
    void testGenericReturnsMap() throws Exception {
        // Copy Excel and JSON files from classpath to temp files
        InputStream excelStream = getClass().getResourceAsStream("/files/simple_excle.xlsx");
        Path excelFile = Files.createTempFile("test", ".xlsx");
        Files.copy(excelStream, excelFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

        InputStream jsonStream = getClass().getResourceAsStream("/def1.json");
        Path jsonFile = Files.createTempFile("test", ".json");
        Files.copy(jsonStream, jsonFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);


        QuickExcelReader reader = QuickExcelReader.builder()
                .excelPath(excelFile)
                .jsonPath(jsonFile)
                .build();

        ResultExcelData result = reader.read();

        System.out.println("Result: " + result);

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


        assertNotNull(result);

        Files.deleteIfExists(excelFile);
        Files.deleteIfExists(jsonFile);
    }
}