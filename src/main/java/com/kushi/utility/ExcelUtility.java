package com.kushi.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.mockito.internal.stubbing.answers.ThrowsException;

public class ExcelUtility {
    private static XSSFSheet excelSheet;
    private static XSSFWorkbook excelWorkbook;
    private static final Logger LOG = LogManager.getLogger(ExcelUtility.class);

    /** The base project path. */
    static final String  baseProjectPath = System.getProperty(Constants.USER_DIR);
    static final String  filepath = baseProjectPath.concat(Constants.TEST_DATA_PATH_QA);

    private static void loadExcelFile() throws IOException {
        LOG.info("Loading the Excel workbook.");
        try (FileInputStream excelFile = new FileInputStream(new File(filepath).getAbsolutePath())) {
            excelWorkbook = new XSSFWorkbook(excelFile);
        }
        excelSheet = excelWorkbook.getSheet("testData");
    }

    private static int findDataRow(String dataKey, int dataColumn) {
        LOG.info("Searching for data row with key: " + dataKey);
        int rowCount = excelSheet.getLastRowNum();
        for (int row = 0; row <= rowCount; row++) {
            if (getCellData(row, dataColumn).equalsIgnoreCase(dataKey)) {
                return row;
            }
        }
        return -1; // Return -1 if not found
    }

    private static String getCellData(int rowNumber, int colNumber) {
        Cell cell = excelSheet.getRow(rowNumber).getCell(colNumber);
        if (cell != null) {
            if (cell.getCellType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue());
            } else if (cell.getCellType() == CellType.STRING) {
                return cell.getStringCellValue();
            }
        }
        return ""; // Return empty string if cell is null or not of type STRING or NUMERIC
    }

    public static void setCellData(String result, int rowNumber, int colNumber, String sheetPath, String sheetName) {
        try {
            Row row = excelSheet.getRow(rowNumber);
            Cell cell = row.getCell(colNumber);
            LOG.info("Setting result in Excel sheet.");
            if (cell == null) {
                cell = row.createCell(colNumber);
            }
            cell.setCellValue(result);

            try (FileOutputStream fileOut = new FileOutputStream(sheetPath + sheetName)) {
                excelWorkbook.write(fileOut);
            }

        } catch (IOException exp) {
            LOG.error("Exception occurred in setCellData: " + exp.getMessage(), exp);
        }
    }

    public static Map<String, String> getData(String dataKey) throws FileNotFoundException{
        Map<String, String> dataMap = new HashMap<>();
        try {
			loadExcelFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
        int dataRow = findDataRow(dataKey.trim(), 0);

        if (dataRow == -1) {
            throw new FileNotFoundException("NO DATA FOUND for dataKey: " + dataKey);
        }

        LOG.info("Test Data Found in Row: " + dataRow);
        int columnCount = excelSheet.getRow(0).getLastCellNum();

        for (int i = 0; i < columnCount; i++) {
            String cellData = getCellData(dataRow, i);
            String columnHeader = getCellData(0, i);
            dataMap.put(columnHeader, cellData);
        }

        return dataMap;
    }

    public static void main(String[] args) throws Exception {
        Map<String, String> dataMap = getData("updateBooking2");
        dataMap.forEach((key, value) -> LOG.info(key + " ==> " + value));
    }
}
