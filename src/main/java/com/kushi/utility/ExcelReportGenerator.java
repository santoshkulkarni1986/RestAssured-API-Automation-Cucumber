package com.kushi.utility;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelReportGenerator {

    private Workbook workbook;
    private Sheet sheet;
    private int rowIndex;

    public void generateReport(List<TestData> testDataList, String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook = new XSSFWorkbook();
            sheet = workbook.createSheet("Test Report");
            rowIndex = 0;

            // Write system info
            writeSystemInfo();

            // Write report header
            createHeaderRow();
            rowIndex++;

            // Write test data
            for (TestData testData : testDataList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(testData.getTestName());
                row.createCell(1).setCellValue(testData.getStatus());
                row.createCell(2).setCellValue(testData.getDetails());
            }

            workbook.write(fileOut);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void writeSystemInfo() {
        Row infoRow = sheet.createRow(rowIndex++);
        infoRow.createCell(0).setCellValue("System Info:");
        infoRow.createCell(1).setCellValue("OS: Windows 11");
        infoRow.createCell(2).setCellValue("URL: https://restful-booker.herokuapp.com");
        infoRow.createCell(3).setCellValue("Owner: Santosh Kulkarni");
        infoRow.createCell(4).setCellValue("Role: Automation Lead");

        rowIndex++; // Add a blank row after the system info
    }

    private void createHeaderRow() {
        Row headerRow = sheet.createRow(rowIndex);
        headerRow.createCell(0).setCellValue("Test Name");
        headerRow.createCell(1).setCellValue("Status");
        headerRow.createCell(2).setCellValue("Details");
    }

    // Class to represent test data
    public static class TestData {
        private final String testName;
        private final String status;
        private final String details;

        public TestData(String testName, String status, String details) {
            this.testName = testName;
            this.status = status;
            this.details = details;
        }

        public String getTestName() {
            return testName;
        }

        public String getStatus() {
            return status;
        }

        public String getDetails() {
            return details;
        }
    }
}
