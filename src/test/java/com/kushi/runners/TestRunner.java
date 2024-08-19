package com.kushi.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "pretty:target/cucumber/cucumber.txt",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", "html:target/cucumber/report",
		"json:target/cucumber/cucumber.json",
		"com.kushi.utility.CustomListener" }, features = { "src/test/java/features" }, glue = { "com.kushi.stepdefinitions" }
// ,dryRun = true, It is used to check if any stepdefinitions missed to
// implement
		, monochrome = true, snippets = SnippetType.CAMELCASE, tags = "@BookerAPI", publish = true)
public class TestRunner {

}