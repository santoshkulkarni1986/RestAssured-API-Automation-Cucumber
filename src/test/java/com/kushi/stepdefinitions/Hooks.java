package com.kushi.stepdefinitions;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

public class Hooks {

	private static final Logger LOG = LogManager.getLogger(Hooks.class);
	
	@Before
	public void startTheTest(Scenario scenario) {
		LOG.info("*****************************************************************************************");
		LOG.info("	Scenario: "+scenario.getName());
		LOG.info("*****************************************************************************************");
	}
}
