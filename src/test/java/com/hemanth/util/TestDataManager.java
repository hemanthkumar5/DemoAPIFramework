package com.hemanth.util;

import com.hemanth.config.ConfigManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Test Data Manager for fetching test data from Excel and JSON files
 */
public class TestDataManager {
    
    private static TestDataManager instance;
    private final ConfigManager config;
    private final Map<String, Object> cachedData;
    
    private TestDataManager() {
        this.config = ConfigManager.getInstance();
        this.cachedData = new HashMap<>();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized TestDataManager getInstance() {
        if (instance == null) {
            instance = new TestDataManager();
        }
        return instance;
    }
    
    /**
     * Read Excel file and return data as List of Maps
     */
    public List<Map<String, Object>> readExcelFile(String filePath, String sheetName) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in file: " + filePath);
            }
            
            return extractDataFromSheet(sheet);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }
    
    /**
     * Read Excel file from classpath
     */
    public List<Map<String, Object>> readExcelFromClasspath(String resourcePath, String sheetName) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
             Workbook workbook = new XSSFWorkbook(is)) {
            
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in resource: " + resourcePath);
            }
            
            return extractDataFromSheet(sheet);
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel resource: " + resourcePath, e);
        }
    }
    
    /**
     * Extract data from Excel sheet
     */
    private List<Map<String, Object>> extractDataFromSheet(Sheet sheet) {
        List<Map<String, Object>> data = new ArrayList<>();
        
        // Get header row
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            return data;
        }
        
        List<String> headers = new ArrayList<>();
        for (Cell cell : headerRow) {
            headers.add(getCellValueAsString(cell));
        }
        
        // Get data rows
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Map<String, Object> rowData = new HashMap<>();
                for (int j = 0; j < headers.size(); j++) {
                    Cell cell = row.getCell(j);
                    rowData.put(headers.get(j), getCellValue(cell));
                }
                data.add(rowData);
            }
        }
        
        return data;
    }
    
    /**
     * Get cell value as appropriate type
     */
    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    double value = cell.getNumericCellValue();
                    if (value == (long) value) {
                        return (long) value;
                    } else {
                        return value;
                    }
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK:
                return "";
            default:
                return null;
        }
    }
    
    /**
     * Get cell value as string
     */
    private String getCellValueAsString(Cell cell) {
        Object value = getCellValue(cell);
        return value != null ? value.toString() : "";
    }
    
    /**
     * Read JSON file and return data
     */
    public <T> T readJsonFile(String filePath, Class<T> clazz) {
        return JsonUtils.fromJsonFile(filePath, clazz);
    }
    
    /**
     * Read JSON file from classpath
     */
    public <T> T readJsonFromClasspath(String resourcePath, Class<T> clazz) {
        return JsonUtils.fromJsonClasspath(resourcePath, clazz);
    }
    
    /**
     * Read test data from Excel with caching
     */
    public List<Map<String, Object>> getTestDataFromExcel(String filePath, String sheetName) {
        String cacheKey = filePath + ":" + sheetName;
        
        if (cachedData.containsKey(cacheKey)) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> cached = (List<Map<String, Object>>) cachedData.get(cacheKey);
            return new ArrayList<>(cached);
        }
        
        List<Map<String, Object>> data = readExcelFile(filePath, sheetName);
        cachedData.put(cacheKey, new ArrayList<>(data));
        return data;
    }
    
    /**
     * Get test data by row index
     */
    public Map<String, Object> getTestDataByRow(String filePath, String sheetName, int rowIndex) {
        List<Map<String, Object>> allData = getTestDataFromExcel(filePath, sheetName);
        
        if (rowIndex >= 0 && rowIndex < allData.size()) {
            return allData.get(rowIndex);
        }
        
        throw new IndexOutOfBoundsException("Row index " + rowIndex + " is out of bounds. Total rows: " + allData.size());
    }
    
    /**
     * Get test data by condition
     */
    public List<Map<String, Object>> getTestDataByCondition(String filePath, String sheetName, 
                                                           String columnName, Object expectedValue) {
        List<Map<String, Object>> allData = getTestDataFromExcel(filePath, sheetName);
        List<Map<String, Object>> filteredData = new ArrayList<>();
        
        for (Map<String, Object> row : allData) {
            Object value = row.get(columnName);
            if (expectedValue.equals(value)) {
                filteredData.add(row);
            }
        }
        
        return filteredData;
    }
    
    /**
     * Get random test data
     */
    public Map<String, Object> getRandomTestData(String filePath, String sheetName) {
        List<Map<String, Object>> allData = getTestDataFromExcel(filePath, sheetName);
        
        if (allData.isEmpty()) {
            throw new RuntimeException("No test data found in sheet: " + sheetName);
        }
        
        Random random = new Random();
        int randomIndex = random.nextInt(allData.size());
        return allData.get(randomIndex);
    }
    
    /**
     * Get test data for specific test scenario
     */
    public Map<String, Object> getTestDataForScenario(String filePath, String sheetName, String scenarioName) {
        return getTestDataByCondition(filePath, sheetName, "scenario", scenarioName).get(0);
    }
    
    /**
     * Get all test scenarios
     */
    public List<String> getAllTestScenarios(String filePath, String sheetName) {
        List<Map<String, Object>> allData = getTestDataFromExcel(filePath, sheetName);
        Set<String> scenarios = new HashSet<>();
        
        for (Map<String, Object> row : allData) {
            Object scenario = row.get("scenario");
            if (scenario != null) {
                scenarios.add(scenario.toString());
            }
        }
        
        return new ArrayList<>(scenarios);
    }
    
    /**
     * Get test data for data-driven testing
     */
    public Object[][] getTestDataForDataProvider(String filePath, String sheetName, String... columnNames) {
        List<Map<String, Object>> allData = getTestDataFromExcel(filePath, sheetName);
        Object[][] dataProvider = new Object[allData.size()][columnNames.length];
        
        for (int i = 0; i < allData.size(); i++) {
            Map<String, Object> row = allData.get(i);
            for (int j = 0; j < columnNames.length; j++) {
                dataProvider[i][j] = row.get(columnNames[j]);
            }
        }
        
        return dataProvider;
    }
    
    /**
     * Write test data to Excel file
     */
    public void writeTestDataToExcel(String filePath, String sheetName, List<Map<String, Object>> data) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            
            if (!data.isEmpty()) {
                // Write headers
                Set<String> headers = data.get(0).keySet();
                Row headerRow = sheet.createRow(0);
                int colIndex = 0;
                for (String header : headers) {
                    Cell cell = headerRow.createCell(colIndex++);
                    cell.setCellValue(header);
                }
                
                // Write data
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Map<String, Object> rowData = data.get(i);
                    colIndex = 0;
                    for (String header : headers) {
                        Cell cell = row.createCell(colIndex++);
                        Object value = rowData.get(header);
                        setCellValue(cell, value);
                    }
                }
            }
            
            // Auto-size columns
            if (!data.isEmpty()) {
                int columnCount = data.get(0).size();
                for (int i = 0; i < columnCount; i++) {
                    sheet.autoSizeColumn(i);
                }
            }
            
            // Write to file
            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to write Excel file: " + filePath, e);
        }
    }
    
    /**
     * Set cell value with appropriate type
     */
    private void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Number) {
            if (value instanceof Integer || value instanceof Long) {
                cell.setCellValue(((Number) value).longValue());
            } else {
                cell.setCellValue(((Number) value).doubleValue());
            }
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof java.util.Date) {
            cell.setCellValue((java.util.Date) value);
        } else {
            cell.setCellValue(value.toString());
        }
    }
    
    /**
     * Clear cache
     */
    public void clearCache() {
        cachedData.clear();
    }
    
    /**
     * Get cache size
     */
    public int getCacheSize() {
        return cachedData.size();
    }
    
    /**
     * Check if file exists
     */
    public boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * Get file size
     */
    public long getFileSize(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : -1;
    }
    
    /**
     * List all sheets in Excel file
     */
    public List<String> getSheetNames(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            List<String> sheetNames = new ArrayList<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                sheetNames.add(workbook.getSheetName(i));
            }
            return sheetNames;
            
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
    }
}
