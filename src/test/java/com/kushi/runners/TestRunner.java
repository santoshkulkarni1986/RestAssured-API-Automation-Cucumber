package com.kushi.runners;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:", 
		"com.kushi.utility.CustomListener" }, features = { "src/test/java/features" }, glue = { "com.kushi.stepdefinitions" }
		, monochrome = true, snippets = SnippetType.CAMELCASE, tags = "@BookerAPI", publish = true)
public class TestRunner {

}
