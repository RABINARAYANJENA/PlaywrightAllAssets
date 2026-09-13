package com.saucedemo.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelEngine {
    
    private static String dataDirectory;
    private static String activeEnvironment;
    private static final Map<String, String> globalRuntimeStorage = new HashMap<>();

    static {
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            Properties props = new Properties();
            props.load(fis);
            dataDirectory = props.getProperty("excel.testdata.directory");
            activeEnvironment = props.getProperty("execution.environment");
        } catch (IOException e) {
            throw new RuntimeException("CRITICAL: Could not process base config.properties parameters.", e);
        }
    }

    public static List<String> getExecutableScenarios() throws IOException {
        List<String> activeScenarios = new ArrayList<>();
        String targetSuitePath = dataDirectory + "TestSuite.xlsx";

        try (FileInputStream fis = new FileInputStream(targetSuitePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheet("Suite");
            int lastRow = sheet.getLastRowNum();
            for (int i = 1; i <= lastRow; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) continue;

                XSSFCell testCaseIdCell = row.getCell(0);
                XSSFCell executionFlagCell = row.getCell(1);

                if (testCaseIdCell != null && executionFlagCell != null) {
                    if (executionFlagCell.getStringCellValue().trim().equalsIgnoreCase("Yes")) {
                        activeScenarios.add(testCaseIdCell.getStringCellValue().trim());
                    }
                }
            }
        }
        return activeScenarios;
    }

    public static void loadGlobalConfigurations() throws IOException {
        String targetGlobalPath = dataDirectory + "GlobalData.xlsx";

        try (FileInputStream fis = new FileInputStream(targetGlobalPath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheet("GlobalConfig");
            XSSFRow headerRow = sheet.getRow(0);
            int targetEnvRowIndex = -1;

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                XSSFRow currentRow = sheet.getRow(i);
                if (currentRow != null && currentRow.getCell(0) != null) {
                    if (currentRow.getCell(0).getStringCellValue().trim().equalsIgnoreCase(activeEnvironment)) {
                        targetEnvRowIndex = i;
                        break;
                    }
                }
            }

            if (targetEnvRowIndex == -1) {
                throw new RuntimeException("Environment mapping failed. No row matching profile: " + activeEnvironment);
            }

            XSSFRow matchedValueRow = sheet.getRow(targetEnvRowIndex);
            for (int col = 0; col < headerRow.getLastCellNum(); col++) {
                String key = headerRow.getCell(col).getStringCellValue().trim();
                String value = getCellStringValue(matchedValueRow.getCell(col));
                globalRuntimeStorage.put(key, value);
            }
        }
    }

    public static List<Map<String, String>> getTestDataIterations(String testCaseID) throws IOException {
        List<Map<String, String>> testDataList = new ArrayList<>();
        String targetDataPath = dataDirectory + "TestData.xlsx";

        try (FileInputStream fis = new FileInputStream(targetDataPath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheet("Data");
            XSSFRow headerRow = sheet.getRow(0);
            int totalRows = sheet.getLastRowNum();

            for (int i = 1; i <= totalRows; i++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null || row.getCell(0) == null) continue;

                if (row.getCell(0).getStringCellValue().trim().equalsIgnoreCase(testCaseID)) {
                    Map<String, String> executionRecordMap = new HashMap<>();
                    for (int j = 0; j < row.getLastCellNum(); j++) {
                        if (headerRow.getCell(j) != null) {
                            String headerKey = headerRow.getCell(j).getStringCellValue().trim();
                            String cellContent = getCellStringValue(row.getCell(j));
                            executionRecordMap.put(headerKey, cellContent);
                        }
                    }
                    testDataList.add(executionRecordMap);
                }
            }
        }
        return testDataList;
    }

    public static String getGlobalVal(String key) {
        return globalRuntimeStorage.getOrDefault(key, "");
    }

    private static String getCellStringValue(XSSFCell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long) cell.getNumericCellValue());
        }
        return cell.getStringCellValue().trim();
    }
}