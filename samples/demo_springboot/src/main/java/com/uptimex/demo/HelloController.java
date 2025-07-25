package com.uptimex.demo;

import com.uptimex.QuickExcelReader;
import com.uptimex.utils.ExcelCellData;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {

    @org.springframework.web.bind.annotation.GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }


    private final String UPLOAD_DIR = System.getProperty("user.dir")+"/uploads/";

    @PostMapping("/upload")
    @ResponseBody
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
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
            Files.copy(jsonStream, jsonFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);


            QuickExcelReader reader = QuickExcelReader.builder()
                    .excelPath(filePath.toFile().getAbsolutePath())
                    .jsonPath(jsonFile.toFile().getAbsolutePath())
                    .build();

            Map<String, Map<String, ExcelCellData>> result = reader.read();
            for (Map.Entry<String, Map<String, ExcelCellData>> sheetEntry : result.entrySet()) {
                System.out.println("Sheet: " + sheetEntry.getKey());

                for (Map.Entry<String, ExcelCellData> fieldEntry : sheetEntry.getValue().entrySet()) {
                    //System.out.println("  Field: " + fieldEntry.getKey() + " -> " + fieldEntry.getValue().getValue());
                    ExcelCellData cellData = fieldEntry.getValue();
                    switch (cellData.getCellType()){
                        case TABLE:
                            System.out.println("  table: Field: " + fieldEntry.getKey());
                            List<Map<String,ExcelCellData>> tableData = cellData.getTableValue();
                            for (Map<String, ExcelCellData> row : tableData) {
                                System.out.println("    Row: ");
                                for (Map.Entry<String, ExcelCellData> rowEntry : row.entrySet()) {
                                    //System.out.println("      " + rowEntry.getKey() + " -> " + rowEntry.getValue().getStringValue());
                                    ExcelCellData cellData2 = rowEntry.getValue();
                                    switch (cellData2.getCellType()) {
                                        case STRING:
                                            System.out.println("  String: Field: " + rowEntry.getKey() + " -> " + cellData2.getStringValue());
                                            break;
                                        case LONG:
                                            System.out.println("  Long: Field: " + rowEntry.getKey() + " -> " + cellData2.getLongValue());
                                            break;
                                        case BOOLEAN:
                                            System.out.println("  Boolean : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
                                            break;
                                        case LOCAL_DATE_TIME:
                                            System.out.println("  LocalDateTime : Field: " + rowEntry.getKey() + " -> " + cellData2.getValue());
                                            break;
                                        default:
                                            System.out.println("  Field: " + rowEntry.getKey() + " -> Unknown type");
                                    }
                                }
                            }
                            break;
                        case STRING:
                            System.out.println("  String: Field: " + fieldEntry.getKey() + " -> " + cellData.getStringValue());
                            break;
                        case LONG:
                            System.out.println("  Long: Field: " + fieldEntry.getKey() + " -> " + cellData.getLongValue());
                            break;
                        case BOOLEAN:
                            System.out.println("  Boolean : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
                            break;
                        case LOCAL_DATE_TIME:
                            System.out.println("  LocalDateTime : Field: " + fieldEntry.getKey() + " -> " + cellData.getValue());
                            break;
                        default:
                            System.out.println("  Field: " + fieldEntry.getKey() + " -> Unknown type");
                    }
                }
            }

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file: " + e.getMessage();
        }

//        return "File uploaded successfully: " + file.getOriginalFilename();
    }
}
