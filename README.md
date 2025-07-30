
### Excel Parser Utility (Quick Excel Reader)
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

Add the following to your `pom.xml` file to use the Quick Excel Reader library in your Java project.
```xml

<dependency>
    <groupId>com.github.nemo97</groupId>
    <artifactId>quick-excel-reader</artifactId>
    <version>0.4.beta</version>
</dependency>

```

You need to add the following dependencies to your `pom.xml` file for JSON and Excel parsing, if already not present.
```xml

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>   
<dependency>
    <groupId>org.apache.poi</groupId>
    <artifactId>poi-ooxml</artifactId>
    <version>5.2.3</version>
</dependency>

```

You can use the `QuickExcelReaderTest` test class as a reference. 
```java

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

// print the result as map
System.out.println("Result: " + result.getDataMap());
// print the result as list
        {
def1=
        {
field1=Test1,
field21=5.5,
field3=true,
field2=3,
field4=[{field4b=test_col2, field4c=test_col3, field4a=1}, {field4b=test_col21, field4c=test_col31, field4a=2}]
        }
        }
```
There is also spring boot sample application in the `sample` folder. 
You can run it as a spring boot application.

```bash
cd sample
.\mvnw spring-boot:run
```
There is a simple UI(http://localhost:8080) to upload excel file in `sample/demo_springboot/` folder and json defination file `sample/demo_springboot/src/main/resources` and see the result.


