
### Excel Parser Utility (Quick Excel Reader)
*** It's 2025! Still Excel rules the worlds.*** Everywhere I go, I write something like this
utility to help me parse excel file quickly. So decided to make it public and for myself for future use.

### Features
- **Quickly read excel files**: Read excel files with minimal setup.
- **Supports multiple sheets**: Read data from multiple sheets in an excel file.
- **Customizable data types**: Specify data types for each column to ensure accurate parsing.
- **Error handling**: Handle errors gracefully with detailed error messages.
- **Flexible configuration**: Easily configure the parser by JSON to suit your needs.
- **Lightweight and fast**: Designed for quick parsing without unnecessary overhead.
- **No external dependencies**: Pure Java implementation with no external libraries required.
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

You can use the `QuickExcelReaderTest` test class to as a reference. Here's a simple example.

