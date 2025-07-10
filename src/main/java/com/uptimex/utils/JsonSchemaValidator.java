package com.uptimex.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class JsonSchemaValidator {

    public static final String CONFIG_SCHEMA_JSON = "/config_schema.json";

    public Set<ValidationMessage>  isValid(InputStream  jsonIs) {
        Assert.notNull(jsonIs, "Schema cannot be null");
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);

            JsonSchema jsonSchema = factory.getSchema(
                    JsonSchemaValidator.class.getResourceAsStream(CONFIG_SCHEMA_JSON));

            JsonNode jsonNode = null;

            jsonNode = mapper.readTree(jsonIs);

            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);

            return errors;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
