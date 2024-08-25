package com.kushi.utility;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import io.cucumber.plugin.EventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;

public class EventLisnerUtility implements EventListener {

    private static final Logger LOG = LogManager.getLogger(EventLisnerUtility.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        // Register handlers for various events
        publisher.registerHandlerFor(TestCaseStarted.class, this::onTestCaseStarted);
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    
    private void onTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        String scenarioName = testCase.getName();
        LOG.info("*****************************************************************************************");
        LOG.info("    Scenario Started: " + scenarioName);
        LOG.info("*****************************************************************************************");
    }

    private void onTestCaseFinished(TestCaseFinished event) {
        TestCase testCase = event.getTestCase();
        Result result = event.getResult();
        Status status = result.getStatus();
        Throwable error = result.getError();

        String scenarioName = testCase.getName();

        logTestResult(scenarioName, status, error);
    }

    private void logTestResult(String scenarioName, Status status, Throwable error) {
        LOG.info("*****************************************************************************************");
        LOG.info("    Scenario: " + scenarioName + " --> " + status.name());

        if (status == Status.PASSED) {
            LOG.info("    Result: PASSED");
        } else if (status == Status.FAILED) {
            LOG.error("    Result: FAILED");
            if (error != null) {
                LOG.error("    Error: ", error);  // Log exception with stack trace
            }
        } else {
            LOG.warn("    Result: " + status.name());
        }

        LOG.info("*****************************************************************************************");
    }
}
