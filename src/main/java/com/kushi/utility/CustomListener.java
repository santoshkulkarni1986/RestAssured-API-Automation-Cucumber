package com.kushi.utility;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseFinished;

public class CustomListener implements ConcurrentEventListener {

    private static final Logger LOG = LogManager.getLogger(CustomListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseFinished.class, this::onTestCaseFinished);
    }

    // Renamed method
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

        if (error != null) {
            LOG.error("    Error: ", error);  // Use LOG.error to log exceptions with stack traces
        }

        LOG.info("*****************************************************************************************");
    }
}

