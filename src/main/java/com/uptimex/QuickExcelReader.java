package com.uptimex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uptimex.config.ExcelDefinition;
import com.uptimex.config.FieldDefinition;
import com.uptimex.utils.ExcelCellData;
import com.uptimex.utils.ExcelCellHolder;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.getType;

public class QuickExcelReader {
    private static Logger LOG = org.slf4j.LoggerFactory.getLogger(QuickExcelReader.class);
    private final boolean validateData;
    private final boolean validateSchema;

    private FileInputStream excelInputStream;
    private FileInputStream configInputStream;
//    private String sheetName;

    private QuickExcelReader(Builder builder) {
        this.excelInputStream = builder.excelInputStream;
        this.configInputStream = builder.configInputStream;
        this.validateSchema = builder.validateSchema;
        this.validateData = builder.validateData;
//        this.sheetName = builder.sheetName;
    }

    public Map<String, Map<String, ExcelCellData>> read() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Map<String, Map<String, ExcelCellData>> excelDataMap = new java.util.HashMap<>();

//        LOG.debug("Validating JSON config against schema...");
//        JsonSchemaValidator jsonSchemaValidator = new JsonSchemaValidator();
//
//        Set<ValidationMessage>  validationMessages = jsonSchemaValidator.isValid(configInputStream);
//        LOG.info("Validation completed. Found {} errors.", validationMessages.size());
//
//        if(!validationMessages.isEmpty()){
//            StringBuilder errorMessage = new StringBuilder("Validation errors found in JSON config:\n");
//            for (ValidationMessage message : validationMessages) {
//                errorMessage.append(message.getMessage()).append("\n");
//            }
//            throw new IllegalArgumentException(errorMessage.toString());
//        }

        LOG.debug("JSON config is valid. Proceeding to read Excel file...");
        ExcelDefinition excelDefinition = objectMapper.readValue(configInputStream, ExcelDefinition.class);
        LOG.info("Excel definition loaded: schemaVersion={}, sheets={}",
                excelDefinition.getSchemaVersion(), excelDefinition.getSheets().size());

        // validate configs consistency, unique sheet names, unique fields name etc.


        // Here you can process the excelDefinition object as needed
        try (FileInputStream fis = excelInputStream) {

            Workbook workbook = new XSSFWorkbook(fis);

            for (com.uptimex.config.SheetDefinition sheetDefinition : excelDefinition.getSheets()) {

                LOG.info("Processing sheet: {}", sheetDefinition.getSheetName());
                // You can access fields and other properties of the sheetDefinition here
                // For example, you can print field names:
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Sheet definition: {}", sheetDefinition);
                }
//                sheetDefinition.getFields().forEach(field -> LOG.debug("Field: {}", field.getName()));
                Sheet sheet = null;
                if (sheetDefinition.getSheetName() != null && !sheetDefinition.getSheetName().isEmpty()) {
                    sheet = workbook.getSheet(sheetDefinition.getSheetName());
                } else if (sheetDefinition.getSheetIndex() >= 0) {
                    sheet = workbook.getSheetAt(sheetDefinition.getSheetIndex());
                }

                if (sheet == null) {
                    LOG.warn("No valid sheet name or index provided for sheet definition: {}", sheetDefinition);
                    throw new IllegalArgumentException("Invalid sheet definition: " + sheetDefinition.getDescription());
                }

                LOG.info("Reading data from sheet: {}", sheet.getSheetName());

                ExcelCellHolder holder = new ExcelCellHolder(0, 0);
                // Read data from the sheet
                Map<String, ExcelCellData> sheetDataMap = new java.util.HashMap<>();
                sheetDataMap = readFields(sheetDefinition.getFields(), holder, sheet);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Data read from sheet {}: {}", sheet.getSheetName(), sheetDataMap);
                }
                excelDataMap.put(sheet.getSheetName(), sheetDataMap);
            }
        }

        LOG.info("Excel data reading completed.");
        return excelDataMap;
    }

    private Map<String, ExcelCellData> readFields(List<FieldDefinition> fieldDefinitions, ExcelCellHolder holder, Sheet sheet) {

        Map<String, ExcelCellData> sheetDataMap = new HashMap<>();

        for (FieldDefinition fieldDefinition : fieldDefinitions) {

            if (FieldDefinition.DataType.TABLE.getValue().equalsIgnoreCase(fieldDefinition.getType())) {
                // complex type i.e. table
                List<Map<String,ExcelCellData>> tableData = readTableField(sheet,fieldDefinition);
                ExcelCellData excelData = new ExcelCellData();
                excelData.setFieldDefinition(fieldDefinition);
                excelData.setValue(tableData);
                sheetDataMap.put(fieldDefinition.getName(), excelData);

                continue;
            }

            LOG.info("Processing field: {}", fieldDefinition.getName());
            if (fieldDefinition.getXlsColumn() == null || fieldDefinition.getXlsColumn().isEmpty()) {
                LOG.warn("Field name is null or empty in sheet: {}, calculating field {} to next cell in same ROW {} ", sheet.getSheetName(), fieldDefinition.getName(), holder.getRowIndex());
                holder.nextCell();
            } else {
                LOG.info("Moved to Field {} is at column: {}", fieldDefinition.getName(), fieldDefinition.getXlsColumn());
                holder.resetToNewPosition(fieldDefinition.getXlsColumn());
            }

            ExcelCellData excelData = new ExcelCellData();
            excelData.setFieldDefinition(fieldDefinition);
            Object cellValue = readDataFromCell(sheet, fieldDefinition, holder);
            excelData.setValue(cellValue);
            sheetDataMap.put(fieldDefinition.getName(), excelData);
        }

        return sheetDataMap;
    }

    private List<Map<String, ExcelCellData>> readTableField(Sheet sheet, FieldDefinition tableDefinition) {
        List<FieldDefinition> tableDefs =  tableDefinition.getFields();
        if (tableDefs == null || tableDefs.isEmpty()) {
            LOG.warn("No fields defined for table type field: {}", tableDefinition.getName());
            throw new IllegalArgumentException("No fields defined for table type field: " + tableDefinition.getName());
        }
        String xlsColumn = tableDefinition.getXlsColumn(); // starting data column for table
        if(xlsColumn == null || xlsColumn.isEmpty()) {
         throw new IllegalArgumentException("XLS column for table field " + tableDefinition.getName() + " cannot be null or empty");
        }
        String startingCell = "";
        String endingCell = "";
        if(xlsColumn.indexOf("-")>0) {
            // if xlsColumn is a range, we need to split it
            String[] parts = xlsColumn.split("-");
            if(parts.length != 2) {
                throw new IllegalArgumentException("Invalid XLS column range for table field " + tableDefinition.getName() + ": " + xlsColumn);
            }
            startingCell = parts[0]; // use the first part as the starting column
            endingCell = parts[1]; // use the 2nd part as the ending column
        }
        ExcelCellHolder tableHolderStart = new ExcelCellHolder(startingCell);
        ExcelCellHolder tableHolderEnd = new ExcelCellHolder(endingCell);
        if (tableHolderStart.getRowIndex() < 0 || tableHolderStart.getColumnIndex() < 0) {
            throw new IllegalArgumentException("Invalid starting cell for table field " + tableDefinition.getName() + ": " + xlsColumn);
        }else if(tableHolderStart.getRowIndex() > tableHolderEnd.getRowIndex()){
            throw new IllegalArgumentException("Invalid starting cell for table field " + tableDefinition.getName() + ": " + xlsColumn);
        }

        List<Map<String, ExcelCellData>> tableDataList = new java.util.ArrayList<>();

        while (tableHolderStart.getRowIndex() <= tableHolderEnd.getRowIndex()) {
            Map<String, ExcelCellData> tableData = new HashMap<>();
            for (int i = 0; i < tableDefs.size(); i++) {
                FieldDefinition colFieldDef = tableDefs.get(i);
                Object cellValue = readDataFromCell(sheet, colFieldDef, tableHolderStart);
                ExcelCellData excelData = new ExcelCellData();
                excelData.setFieldDefinition(colFieldDef);
                excelData.setValue(cellValue);
                tableData.put(colFieldDef.getName(), excelData);
                tableHolderStart.nextCell();
//                if (colFieldDef.getXlsColumn() == null || colFieldDef.getXlsColumn().isEmpty()) {
//                    LOG.warn("Field name is null or empty in table field: {}, calculating field {} to next cell in same ROW {} ", tableDefinition.getName(), colFieldDef.getName(), tableHolderStart.getRowIndex());
//                    tableHolderStart.nextCell();
//                } else {
//                    LOG.info("Moved to Field {} is at column: {}", colFieldDef.getName(), colFieldDef.getXlsColumn());
//                    tableHolderStart.resetToNewPosition(colFieldDef.getXlsColumn());
//                }
            }

            tableDataList.add(tableData);
            tableHolderStart.setColumnIndex(tableHolderEnd.getColumnIndex());
            tableHolderStart.nextRow(); // Move to the next row for the next iteration
        }

        return tableDataList;
    }

    private Object readDataFromCell(Sheet sheet, FieldDefinition fieldDefinition, ExcelCellHolder holder) {
        Row row = sheet.getRow(holder.getRowIndex());
        if (row == null) return null;
        Cell cell = row.getCell(holder.getColumnIndex());
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                String cellValue = cell.getStringCellValue();
                if(fieldDefinition.getDataType() == FieldDefinition.DataType.STRING) {
                    return cellValue;
                }
                break;
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    Date cellDtValue = cell.getDateCellValue();
                    if (fieldDefinition.getDataType() == FieldDefinition.DataType.LOCAL_DATE_TIME) {
                        return cellDtValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDateTime();
                    } else if (fieldDefinition.getDataType() == FieldDefinition.DataType.LOCAL_DATE) {
                        return cellDtValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    }
                }
                double doubleCellVal = cell.getNumericCellValue();
                if (fieldDefinition.getDataType() == FieldDefinition.DataType.DOUBLE) {
                    return new Double(doubleCellVal);
                } else if (fieldDefinition.getDataType() == FieldDefinition.DataType.LONG) {
                    return new Double(doubleCellVal).longValue(); // Convert to long if needed
                }
                break;
            case BOOLEAN:
                return cell.getBooleanCellValue();

            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return cell.toString();
        }

        return null; // Default case if no type matches
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private FileInputStream excelInputStream;
        private FileInputStream configInputStream;
        private boolean validateSchema = true;
        private boolean validateData;

        public Builder excelPath(String excelPath) {
//            this.excelPath = excelPath;
            try {
                this.excelInputStream = new FileInputStream(excelPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open Excel file: " + excelPath, e);
            }
            return this;
        }

        public Builder jsonPath(String jsonPath) {
//            this.jsonPath = jsonPath;
            try {
                this.configInputStream = new FileInputStream(jsonPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to open JSON config file: " + jsonPath, e);
            }
            return this;
        }

        public Builder excelInputStream(FileInputStream excelInputStream) {
            this.excelInputStream = excelInputStream;
            return this;
        }

        public Builder configInputStream(FileInputStream configInputStream) {
            this.configInputStream = configInputStream;
            return this;
        }

        public Builder validateSchema(boolean validateSchema) {
            this.validateSchema = validateSchema;
            return this;
        }

        public Builder validateData(boolean validateData) {
            this.validateData = validateData;
            return this;
        }


        public QuickExcelReader build() {
            if (excelInputStream == null) {
                throw new IllegalArgumentException("Excel input stream must not be null");
            }
            if (configInputStream == null) {
                throw new IllegalArgumentException("Config input stream must not be null");
            }
            return new QuickExcelReader(this);
        }
    }
}