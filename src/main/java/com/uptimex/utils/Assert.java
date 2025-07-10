package com.uptimex.utils;

import java.io.InputStream;

public class Assert {
    public static void notNull(String cellName, String cellNameCannotBeNull) {
        if(cellName == null || cellName.isEmpty()) {
            throw new IllegalArgumentException(cellNameCannotBeNull);
        }
    }

    public static void notNull(InputStream jsonIs, String schemaCannotBeNull) {
        if(jsonIs == null) {
            throw new IllegalArgumentException(schemaCannotBeNull);
        }
    }
}
