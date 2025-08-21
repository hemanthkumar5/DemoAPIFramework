package com.hemanth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Utility class for JSON serialization and deserialization operations
 */
public class JsonUtils {

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        
        // Configure ObjectMapper
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        
        // Configure for better JSON handling
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    /**
     * Get the configured ObjectMapper instance
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Serialize object to JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to JSON", e);
        }
    }

    /**
     * Serialize object to pretty-printed JSON string
     */
    public static String toPrettyJson(Object object) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize object to pretty JSON", e);
        }
    }

    /**
     * Deserialize JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + clazz.getSimpleName(), e);
        }
    }

    /**
     * Deserialize JSON string to object using TypeReference (for generics)
     */
    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to " + typeReference.getType(), e);
        }
    }

    /**
     * Deserialize JSON string to List of objects
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to List<" + clazz.getSimpleName() + ">", e);
        }
    }

    /**
     * Deserialize JSON string to Map
     */
    public static Map<String, Object> fromJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize JSON to Map", e);
        }
    }

    /**
     * Deserialize JSON file to object
     */
    public static <T> T fromJsonFile(String filePath, Class<T> clazz) {
        try {
            return objectMapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }

    /**
     * Deserialize JSON file to object
     */
    public static <T> T fromJsonFile(File file, Class<T> clazz) {
        try {
            return objectMapper.readValue(file, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + file.getPath(), e);
        }
    }

    /**
     * Deserialize JSON from InputStream to object
     */
    public static <T> T fromJsonInputStream(InputStream inputStream, Class<T> clazz) {
        try {
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from InputStream", e);
        }
    }

    /**
     * Deserialize JSON from classpath resource to object
     */
    public static <T> T fromJsonClasspath(String resourcePath, Class<T> clazz) {
        try (InputStream inputStream = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: " + resourcePath);
            }
            return objectMapper.readValue(inputStream, clazz);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON from classpath resource: " + resourcePath, e);
        }
    }

    /**
     * Write object to JSON file
     */
    public static void writeJsonToFile(Object object, String filePath) {
        try {
            objectMapper.writeValue(new File(filePath), object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write JSON to file: " + filePath, e);
        }
    }

    /**
     * Write object to pretty-printed JSON file
     */
    public static void writePrettyJsonToFile(Object object, String filePath) {
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), object);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write pretty JSON to file: " + filePath, e);
        }
    }

    /**
     * Convert object to Map
     */
    public static Map<String, Object> toMap(Object object) {
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

    /**
     * Convert Map to object
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * Deep copy object using serialization
     */
    public static <T> T deepCopy(T object, Class<T> clazz) {
        String json = toJson(object);
        return fromJson(json, clazz);
    }

    /**
     * Validate JSON string
     */
    public static boolean isValidJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    /**
     * Get JSON node as string
     */
    public static String getJsonNodeAsString(String json, String path) {
        try {
            var jsonNode = objectMapper.readTree(json);
            var node = jsonNode.at(path);
            return node.isMissingNode() ? null : node.asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON or extract node", e);
        }
    }

    /**
     * Merge two JSON strings
     */
    public static String mergeJson(String json1, String json2) {
        try {
            var node1 = objectMapper.readTree(json1);
            var node2 = objectMapper.readTree(json2);
            var merged = objectMapper.createObjectNode();
            merged.setAll((com.fasterxml.jackson.databind.node.ObjectNode) node1);
            merged.setAll((com.fasterxml.jackson.databind.node.ObjectNode) node2);
            return objectMapper.writeValueAsString(merged);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to merge JSON strings", e);
        }
    }
}
