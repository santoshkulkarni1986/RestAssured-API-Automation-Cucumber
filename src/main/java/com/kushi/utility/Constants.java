package com.kushi.utility;

public class Constants {

    // Prevents instantiation
    private Constants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /** The Constant CONFIG_PROPERTY. */
    public static final String CONFIG_PROPERTY = "/src/test/java/properties/config.properties";
    /** The Constant USER_DIR. */
    public static final String USER_DIR = "user.dir";
      /** The Constant TEST_DATA_PATH_QA. */
    public static final String TEST_DATA_PATH_QA = "/src/test/java/testdata/testDataFromExcel.xlsx";
    /** The Constant JSON FILES. */
    public static final String TEST_JSON_FILE_PATH = "/src/test/java/schemas/";
    
}
