package com.request.utils;

/**
 * Utilities
 */
public class Utilities {

    private static final Settings SETTINGS = new Settings();

    public static String prelogString(String identifier, int lineNumber, String logMessage) {
        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String file = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String method = Thread.currentThread().getStackTrace()[2].getMethodName();
        return "APP NAME >> " + SETTINGS.getAppliaction_name() + " | FILE >> " + file + " | CLASS >> " + fullClassName
                + " | METHOD >> " + method + " | LINE >> " + lineNumber + " | IDENTIFIER >> " + identifier
                + " | MESSAGE >> " + logMessage;
    }

    /**
     * getCodelineNumber
     *
     * @return lineNumber
     */
    public static int getCodelineNumber() {
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        return lineNumber;
    }
}