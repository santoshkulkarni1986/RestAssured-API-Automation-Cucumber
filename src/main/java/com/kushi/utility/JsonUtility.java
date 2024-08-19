package com.kushi.utility;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.io.FileInputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.apache.log4j.Logger;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ReadContext;

public class JsonUtility {

    private static final Logger LOG = Logger.getLogger(JsonUtility.class);
    private static final JSONParser parser = new JSONParser();

    /**
     * Loads the properties from a specified file path.
     * 
     * @return Properties object loaded with values from the file.
     */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(System.getProperty(Constants.USER_DIR) + Constants.CONFIG_PROPERTY)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties file: " + Constants.CONFIG_PROPERTY, e);
        }
        return properties;
    }
    
    /**
     * Retrieves the request body from a JSON file based on the given key.
     *
     * @param jsonFileName the JSON file name
     * @param jsonKey      the key whose value needs to be fetched
     * @return the value associated with the key as a String
     */
    public static String getRequestBody(String jsonFileName, String jsonKey) {
        final String baseProjectPath = System.getProperty(Constants.USER_DIR);
        final String filepath = baseProjectPath.concat(Constants.TEST_JSON_FILE_PATH);

        try (FileReader reader = new FileReader(filepath + jsonFileName)) {
            Object body = ((JSONObject) parser.parse(reader)).get(jsonKey);
            if (body == null) {
                throw new RuntimeException("NO DATA FOUND in JSON file '" + jsonFileName + "' for key '" + jsonKey + "'");
            }
            return body.toString();
        } catch (IOException | ParseException e) {
            LOG.error("Error while reading or parsing the file: " + jsonFileName, e);
            throw new RuntimeException("Error processing JSON file: " + jsonFileName, e);
        }
    }

    /**
     * Verifies that the value of a given key in a JSON object matches the expected value.
     *
     * @param jsonResponse  the JSON response as a String
     * @param keyPath       the path to the key (can be nested, e.g., "bookingdates.checkin")
     * @param expectedValue the expected value
     * @return true if the value matches the expected value, false otherwise
     */
    public static boolean verifyJsonKeyValue(String jsonResponse, String keyPath, Object expectedValue) {
        try {
            ReadContext ctx = JsonPath.parse(jsonResponse);
            Object actualValue = ctx.read(keyPath);
            return expectedValue.equals(actualValue);
        } catch (Exception e) {
            LOG.error("Error while verifying JSON key value for keyPath: " + keyPath, e);
            return false;
        }
    }

    /**
     * Verifies that a given key path exists in a JSON object.
     *
     * @param jsonResponse the JSON response as a String
     * @param keyPath      the key path to check
     * @return true if the key exists, false otherwise
     */
    public static boolean verifyJsonKeyExists(String jsonResponse, String keyPath) {
        try {
            ReadContext ctx = JsonPath.parse(jsonResponse);
            ctx.read(keyPath);
            return true;
        } catch (Exception e) {
            LOG.error("Key does not exist: " + keyPath, e);
            return false;
        }
    }

    /**
     * Verifies that a given value exists in a JSON array.
     *
     * @param jsonResponse  the JSON response as a String
     * @param arrayKeyPath  the path to the array key
     * @param expectedValue the expected value to find in the array
     * @return true if the value exists in the array, false otherwise
     */
    public static boolean verifyJsonArrayContains(String jsonResponse, String arrayKeyPath, Object expectedValue) {
        try {
            ReadContext ctx = JsonPath.parse(jsonResponse);
            List<Object> jsonArray = ctx.read(arrayKeyPath);
            return jsonArray.contains(expectedValue);
        } catch (Exception e) {
            LOG.error("Error while verifying JSON array value for keyPath: " + arrayKeyPath, e);
            return false;
        }
    }

    /**
     * Verifies a value in a nested JSON array.
     *
     * @param jsonResponse  the JSON response as a String
     * @param keyPath       the path to the key in the nested array
     * @param expectedValue the expected value
     * @return true if the value matches the expected value, false otherwise
     */
    public static boolean verifyNestedJsonArrayValue(String jsonResponse, String keyPath, Object expectedValue) {
        try {
            ReadContext ctx = JsonPath.parse(jsonResponse);
            List<Object> values = ctx.read(keyPath);
            return values.stream().anyMatch(value -> expectedValue.equals(value));
        } catch (Exception e) {
            LOG.error("Error while verifying nested JSON array value for keyPath: " + keyPath, e);
            return false;
        }
    }
}
