
### WIP - Excel Parser Utility (Quick Excel Reader)
**It's 2025! Still The World run on Excel.** Everywhere I go, I write something like this
utility to help me parse excel file quickly. So decided to make it public and for myself for future use.

### Features
- **Quickly read excel files**: Read excel files with minimal setup.
- **Supports multiple sheets**: Read data from multiple sheets in an excel file.
- **Customizable data types**: Specify data types for each column to ensure accurate parsing.
- **Error handling**: Handle errors gracefully with detailed error messages.
- **Flexible configuration**: Easily configure the parser by JSON to suit your needs.
- **Lightweight and fast**: Designed for quick parsing without unnecessary overhead.
- **Minimal external dependencies**: Pure Java implementation with popular JSON and Excel dependencies.
- **Validation(WIP)**: Validate data types and handle missing values.
- **Documentation**: Comprehensive documentation to help you get started quickly.

### Quick Start

This is very early version of the project. Currently, you can use it as a library in your Java project
by building from source code.

#### Build from Source
   ```bash
   git clone https://github.com/nemo97/QuickExcelReader.git
   cd <checkout folder>/QuickExcelReader
   mvnw clean install
   ```
#### Usage

You can use the `QuickExcelReaderTest` test class as a reference. 
```java

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

        Map<String,Map<String, ExcelCellData>> result = reader.read();

        //System.out.println("Result: " + result);

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

```

