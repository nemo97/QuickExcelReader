package com.uptimex.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.uptimex.QuickExcelReader;
import com.uptimex.ResultExcelData;
import com.uptimex.demo.dtos.SampleExcelData;
import com.uptimex.demo.dtos.SampleExcelSheetData;
import com.uptimex.demo.dtos.SampleExcelSheetTableData;
import com.uptimex.utils.ExcelCellData;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;



@Controller
public class Hello2Controller {

    private final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    @RequestMapping(value = "/")
    public String welcome() {
        return "index"; // Redirect to the index page or any other view
    }

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public String handleFileUpload(Model model, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }
        // You can add logic here to process the Excel file

        try {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadPath);

            // Construct the target file path
            Path filePath = Paths.get(UPLOAD_DIR, file.getOriginalFilename());

            // Save the file to disk
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Optionally, you can add logic to read the Excel file and process it
            // For example, using QuickExcelReader or any other library to read the file
            // Copy Excel and JSON files from classpath to temp files
//            InputStream excelStream = filePath.toUri().toURL().openStream();
//            Path excelFile = Files.createTempFile("test", ".xlsx");
//            Files.copy(excelStream, excelFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            InputStream jsonStream = getClass().getResourceAsStream("/def1.json");
            Path jsonFile = Files.createTempFile("test", ".json");
            Files.copy(jsonStream, jsonFile, StandardCopyOption.REPLACE_EXISTING);


            QuickExcelReader reader = QuickExcelReader.builder()
                    .excelPath(filePath)
                    .jsonPath(jsonFile)
                    .build();

            ResultExcelData result = reader.read();
            SampleExcelData sampleExcelData = new SampleExcelData();
            System.out.println("Result: " + result);
            try {
                Map<String, Map<String, Object>>  parseData = result.getDataMap();
                for (Map.Entry<String, Map<String, Object>> sheetEntry : parseData.entrySet()) {

                    Map<String, Object> sheetData = sheetEntry.getValue();
                    SampleExcelSheetData sampleExcelSheetData = new SampleExcelSheetData();
                    sampleExcelSheetData.setName(sheetEntry.getKey());
                    sampleExcelData.getSheets().add(sampleExcelSheetData);
                    BeanUtils.copyProperties(sampleExcelSheetData, sheetData);

                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
//            List<SampleExcelSheetData>  sheetDataList = sampleExcelData.getSheets();

//            for (Map.Entry<String, Map<String, ExcelCellData>> sheetEntry : result.entrySet()) {
//                System.out.println("Sheet: " + sheetEntry.getKey());
//                SampleExcelSheetData sampleExcelSheetData = new SampleExcelSheetData();
//                sampleExcelSheetData.setName(sheetEntry.getKey());
//                sheetDataList.add(sampleExcelSheetData);
//
//                for (Map.Entry<String, ExcelCellData> fieldEntry : sheetEntry.getValue().entrySet()) {
//                    //System.out.println("  Field: " + fieldEntry.getKey() + " -> " + fieldEntry.getValue().getValue());
//                    ExcelCellData cellData = fieldEntry.getValue();
//                    switch (cellData.getCellType()){
//                        case TABLE:
//                            System.out.println("  table: Field: " + fieldEntry.getKey());
//                            List< SampleExcelSheetTableData> tableDataList = sampleExcelSheetData.getField4();
//
//
//                            List<Map<String,ExcelCellData>> tableData = cellData.getTableValue();
//                            for (Map<String, ExcelCellData> row : tableData) {
//                                System.out.println("    Row: ");
//                                SampleExcelSheetTableData sampleExcelSheetTableData = new SampleExcelSheetTableData();
//                                for (Map.Entry<String, ExcelCellData> rowEntry : row.entrySet()) {
//                                    //System.out.println("      " + rowEntry.getKey() + " -> " + rowEntry.getValue().getStringValue());
//                                    ExcelCellData cellData2 = rowEntry.getValue();
//                                    switch (cellData2.getCellType()) {
//                                        case STRING:
//                                            System.out.println("  String: Field: " + rowEntry.getKey() + " -> " + cellData2.getStringValue());
//                                            populateField(sampleExcelSheetTableData, rowEntry.getKey(), cellData2.getValue());
//                                            break;
//                                        case LONG:
//                                            System.out.println("  Long: Field: " + rowEntry.getKey() + " -> " + cellData2.getLongValue());
//                                            populateField(sampleExcelSheetTableData, rowEntry.getKey(), cellData2.getValue());
//                                            break;
//                                        case BOOLEAN:
//                                            System.out.println("  Boolean : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
//                                            populateField(sampleExcelSheetTableData, rowEntry.getKey(), cellData2.getValue());
//                                            break;
//                                        case LOCAL_DATE_TIME:
//                                            System.out.println("  LocalDateTime : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
//                                            populateField(sampleExcelSheetTableData, rowEntry.getKey(), cellData2.getValue());
//                                            break;
//                                        default:
//                                            System.out.println("  Field: " + rowEntry.getKey() + " -> Unknown type");
//                                    }
//                                }
//
//                                tableDataList.add(sampleExcelSheetTableData);
//
//                            }
//                            break;
//                        case STRING:
//                            System.out.println("  String: Field: " + fieldEntry.getKey() + " -> " + cellData.getStringValue());
//                            populateField(sampleExcelSheetData, fieldEntry.getKey(), cellData.getStringValue());
//                            break;
//                        case LONG:
//                            System.out.println("  Long: Field: " + fieldEntry.getKey() + " -> " + cellData.getLongValue());
//                            populateField(sampleExcelSheetData, fieldEntry.getKey(), cellData.getValue());
//
//                            break;
//                        case BOOLEAN:
//                            System.out.println("  Boolean : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
//                            populateField(sampleExcelSheetData, fieldEntry.getKey(), cellData.getValue());
//
//                            break;
//                        case LOCAL_DATE_TIME:
//                            System.out.println("  LocalDateTime : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
//                            populateField(sampleExcelSheetData, fieldEntry.getKey(), cellData.getValue());
//
//                            break;
//                        default:
//                            System.out.println("  Field: " + fieldEntry.getKey() + " -> Unknown type");
//                    }
//                }
//            }
            // Add a success message to the model
            model.addAttribute("message","File uploaded successfully: " + file.getOriginalFilename());
            model.addAttribute("data",objectMapper.writeValueAsString(sampleExcelData));
//            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
//            return "Failed to upload file: " + e.getMessage();
        }

//        return "File uploaded successfully: " + file.getOriginalFilename();
        return "index"; // Return to the index page or any other view
    }

//    private void populateField(Object sampleExcelSheetData, String key, Object stringValue) {
//        try {
//            BeanUtils.setProperty(sampleExcelSheetData,key, stringValue);
//        } catch (IllegalAccessException e) {
//            throw new RuntimeException(e);
//        } catch (InvocationTargetException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
