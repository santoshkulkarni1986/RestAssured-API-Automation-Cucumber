package com.kushi.utility;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonResponseValidator {

    private final ObjectMapper objectMapper;

    public JsonResponseValidator() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Retrieve a value from JSON response based on a dot-separated key.
     * 
     * @param jsonResponse JSON response as a string
     * @param key          Dot-separated key to retrieve value (e.g., "parent.child.array[0].key")
     * @return The value associated with the key
     * @throws IOException If there is an error parsing the JSON
     */
    public Object getValueFromJson(String jsonResponse, String key) throws IOException {
        JsonNode rootNode = objectMapper.readTree(jsonResponse);
        return getValue(rootNode, key);
    }

    private Object getValue(JsonNode node, String key) {
        String[] keys = key.split("\\.");
        JsonNode currentNode = node;

        for (int i = 0; i < keys.length; i++) {
            String currentKey = keys[i];
            if (currentKey.contains("[")) {
                int arrayIndex = Integer.parseInt(currentKey.substring(currentKey.indexOf('[') + 1, currentKey.indexOf(']')));
                String arrayKey = currentKey.substring(0, currentKey.indexOf('['));
                currentNode = currentNode.get(arrayKey).get(arrayIndex);
            } else if (currentNode.has(currentKey)) {
                currentNode = currentNode.get(currentKey);
            } else {
                return null;
            }
        }

        return currentNode.isValueNode() ? currentNode.asText() : currentNode.toString();
    }

    /**
     * Validate the value in the JSON response.
     * 
     * @param jsonResponse JSON response as a string
     * @param key          Dot-separated key to validate value
     * @param expectedValue Expected value
     * @throws IOException If there is an error parsing the JSON
     */
    public void validateResponseValue(String jsonResponse, String key, String expectedValue) throws IOException {
        Object actualValue = getValueFromJson(jsonResponse, key);
        if (actualValue == null || !expectedValue.equals(actualValue.toString())) {
            throw new AssertionError("Expected value for key: " + key + " is: " + expectedValue
                    + " but got: " + actualValue);
        }
    }
}
