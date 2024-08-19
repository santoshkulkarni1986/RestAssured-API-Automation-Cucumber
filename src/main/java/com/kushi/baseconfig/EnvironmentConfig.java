 package com.kushi.baseconfig;

 public class EnvironmentConfig {

     private static final String BASE_URL_DEFAULT = "https://restful-booker.herokuapp.com";
     private static final String ENVIRONMENT_DEFAULT = "Test";
     private static final String USER_NAME_DEFAULT = "admin";
     private static final String PASSWORD_DEFAULT = "password123";

     /**
      * Get the base URL from environment variables or use the default value.
      *
      * @return the base URL
      */
     public static String getBaseUrl() {
         return getEnvVariable("BASE_URL", BASE_URL_DEFAULT);
     }

     /**
      * Get the environment name from environment variables or use the default value.
      *
      * @return the environment name
      */
     public static String getEnvironment() {
         return getEnvVariable("EnvironMent", ENVIRONMENT_DEFAULT);
     }

     /**
      * Get the user name from environment variables or use the default value.
      *
      * @return the user name
      */
     public static String getUserName() {
         return getEnvVariable("User_Name", USER_NAME_DEFAULT);
     }

     /**
      * Get the password from environment variables or use the default value.
      *
      * @return the password
      */
     public static String getPassword() {
         return getEnvVariable("PassWord", PASSWORD_DEFAULT);
     }

     /**
      * Helper method to get environment variable or use a default value.
      *
      * @param variableName the name of the environment variable
      * @param defaultValue the default value if the environment variable is not found
      * @return the value of the environment variable, or the default value if not found
      */
     private static String getEnvVariable(String variableName, String defaultValue) {
         String value = System.getenv(variableName);
         return value != null ? value : defaultValue;
     }
 }
