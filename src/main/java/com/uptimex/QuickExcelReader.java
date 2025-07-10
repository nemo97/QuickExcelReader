package com.uptimex;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.ValidationMessage;
import com.uptimex.config.ExcelDefinition;
import com.uptimex.config.FieldDefinition;
import com.uptimex.utils.ExcelCellData;
import com.uptimex.utils.ExcelCellHolder;
import com.uptimex.utils.ExcelDataInner;
import com.uptimex.utils.JsonSchemaValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class QuickExcelReader {
    private static Logger LOG = org.slf4j.LoggerFactory.getLogger(QuickExcelReader.class);

    private FileInputStream excelInputStream;
    private FileInputStream configInputStream;
//    private String sheetName;

    private QuickExcelReader(Builder builder) {
        this.excelInputStream = builder.excelInputStream;
        this.configInputStream = builder.configInputStream;
//        this.sheetName = builder.sheetName;
    }

    public Map<String,Map<String, ExcelDataInner>> read() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        Map<String,Map<String, ExcelDataInner>> excelDataMap = new java.util.HashMap<>();

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
                if(LOG.isDebugEnabled()){
                    LOG.debug("Sheet definition: {}", sheetDefinition);
                }
//                sheetDefinition.getFields().forEach(field -> LOG.debug("Field: {}", field.getName()));
                Sheet sheet = null;
                if(sheetDefinition.getSheetName() != null && !sheetDefinition.getSheetName().isEmpty()) {
                    sheet = workbook.getSheet(sheetDefinition.getSheetName());
                } else if (sheetDefinition.getSheetIndex() >= 0) {
                    sheet = workbook.getSheetAt(sheetDefinition.getSheetIndex());
                }

                if (sheet == null) {
                    LOG.warn("No valid sheet name or index provided for sheet definition: {}", sheetDefinition);
                    throw new IllegalArgumentException("Invalid sheet definition: " + sheetDefinition.getDescription());
                }

                LOG.info("Reading data from sheet: {}", sheet.getSheetName());

                ExcelCellHolder holder = new ExcelCellHolder(0,0);
                // Read data from the sheet
                Map<String, ExcelDataInner> sheetDataMap = new java.util.HashMap<>();
                for(FieldDefinition fieldDefinition: sheetDefinition.getFields()) {

                    LOG.info("Processing field: {}", fieldDefinition.getName());
                    if(fieldDefinition.getXlsColumn() == null || fieldDefinition.getXlsColumn().isEmpty()) {
                        LOG.warn("Field name is null or empty in sheet: {}, calculating field {} to next cell in same ROW {} ", sheetDefinition.getSheetName(), fieldDefinition.getName(), holder.getRowIndex());
                        holder.nextCell();
                    }else{
                        LOG.info("Moved to Field {} is at column: {}", fieldDefinition.getName(), fieldDefinition.getXlsColumn());
                        holder.resetToNewPosition(fieldDefinition.getXlsColumn());
                    }
                    ExcelDataInner excelDataInner = new ExcelDataInner();
                    ExcelCellData excelData = new ExcelCellData();
                    excelData.setFieldDefinition(fieldDefinition);
                    Object cellValue = readDataFromSheet(sheet, fieldDefinition, holder);
                    excelData.setValue(cellValue);
                    excelDataInner.put(fieldDefinition.getName(), fieldDefinition, excelData);

                    sheetDataMap.put(fieldDefinition.getName(), excelDataInner);
                }
                if(LOG.isDebugEnabled()) {
                    LOG.debug("Data read from sheet {}: {}", sheet.getSheetName(), sheetDataMap);
                }
                excelDataMap.put(sheet.getSheetName(), sheetDataMap);
            }
        }

        LOG.info("Excel data reading completed.");
        return excelDataMap;
    }

    private Object readDataFromSheet(Sheet sheet, FieldDefinition fieldDefinition,ExcelCellHolder holder) {
        Row row = sheet.getRow(holder.getRowIndex());
        if (row == null) return null;
        Cell cell = row.getCell(holder.getColumnIndex());
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return cell.toString();
        }
    }
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private FileInputStream excelInputStream;
        private FileInputStream configInputStream;

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

//        public Builder sheetName(String sheetName) {
//            this.sheetName = sheetName;
//            return this;
//        }

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