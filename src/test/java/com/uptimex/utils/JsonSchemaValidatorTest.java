package com.uptimex.utils;

import com.networknt.schema.ValidationMessage;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonSchemaValidatorTest {

    @Test
    void testValidJson() {
        String validJson = "{ \"schemaVersion\": \"1.0\", \"sheets\": [ { \"sheetName\": \"Sheet1\", \"fields\": [ { \"name\": \"field1\", \"type\": \"string\" } ] } ] }";
        InputStream jsonIs = new ByteArrayInputStream(validJson.getBytes());
        JsonSchemaValidator validator = new JsonSchemaValidator();
        Set<ValidationMessage> errors = validator.isValid(jsonIs);
        assertTrue(errors.isEmpty(), "Expected no validation errors for valid JSON");
    }

    @Test
    void testInvalidJson() {
        String invalidJson = "{ \"schemaVersion\": 1, \"sheets\": [] }"; // schemaVersion should be string
        InputStream jsonIs = new ByteArrayInputStream(invalidJson.getBytes());
        JsonSchemaValidator validator = new JsonSchemaValidator();
        Set<ValidationMessage> errors = validator.isValid(jsonIs);
        assertFalse(errors.isEmpty(), "Expected validation errors for invalid JSON");
    }
}