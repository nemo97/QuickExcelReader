package com.uptimex;
import com.uptimex.QuickExcelReader;
import com.uptimex.utils.ExcelDataInner;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

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
                .excelPath(excelFile.toFile().getAbsolutePath())
                .jsonPath(jsonFile.toFile().getAbsolutePath())
                .build();

        Map<String,Map<String, ExcelDataInner>> result = reader.read();

        System.out.println("Result: " + result);
        assertNotNull(result);

        Files.deleteIfExists(excelFile);
        Files.deleteIfExists(jsonFile);
    }
}