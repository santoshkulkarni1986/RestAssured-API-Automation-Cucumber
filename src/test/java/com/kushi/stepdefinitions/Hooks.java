package com.kushi.stepdefinitions;

import com.kushi.utility.ExcelReportGenerator;
import com.kushi.utility.ExcelReportGenerator.TestData;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

import java.util.ArrayList;
import java.util.List;

public class Hooks {

    private static List<TestData> testDataList = new ArrayList<>();

    @BeforeAll
    public static void setup() {
        // Initialization code if needed
    }

    @After
    public void afterScenario(Scenario scenario) {
        // Collect data for the Excel report
        String status = scenario.isFailed() ? "Failed" : "Passed";
        TestData testData = new TestData(scenario.getName(), status, scenario.getId());
        testDataList.add(testData);
    }

    @AfterAll
    public static void teardown() {
        // Generate Excel report
        ExcelReportGenerator excelReportGenerator = new ExcelReportGenerator();
        excelReportGenerator.generateReport(testDataList, "report/ExtentExcelReport.xlsx");
    }
}
