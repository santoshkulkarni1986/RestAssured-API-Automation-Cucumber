package com.kushi.utility;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class SchemaValidator {
    private static final Logger LOG = LogManager.getLogger(SchemaValidator.class);

    /**
     * Validates a JSON response against a schema file located in src/test/java/schemas.
     *
     * @param response       the REST Assured Response object containing the JSON response
     * @param schemaFileName the name of the JSON schema file
     */
    public static void validateResponseWithSchema(Response response, String schemaFileName) {
        try {
            // Construct the full file path
            String schemaFilePath = "src/test/java/schemas/" + schemaFileName;
            LOG.info("Loading schema from: " + schemaFilePath);

            // Read the schema content
            String schemaContent = new String(Files.readAllBytes(Paths.get(schemaFilePath)));

            // Validate the response against the schema
            response.then().assertThat()
                    .body(JsonSchemaValidator.matchesJsonSchema(schemaContent));

            LOG.info("Successfully validated schema: " + schemaFileName);
        } catch (IOException e) {
            LOG.error("Error loading schema file: " + schemaFileName, e);
            throw new RuntimeException("Error loading schema file: " + schemaFileName, e);
        }
    }
}
