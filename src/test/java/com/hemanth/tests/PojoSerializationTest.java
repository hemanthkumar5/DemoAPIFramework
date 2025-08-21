package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.models.ApiResponse;
import com.hemanth.models.Support;
import com.hemanth.models.User;
import com.hemanth.util.JsonUtils;
import com.hemanth.util.TestDataBuilder;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Test class to demonstrate POJO serialization and deserialization capabilities
 */
public class PojoSerializationTest extends BaseTest {

    @Test
    public void testUserSerialization() {
        // Create a User object
        User user = TestDataBuilder.buildFullUser(1, "John Doe", "Software Engineer", 
                                                 "john.doe@example.com", "John", "Doe", 
                                                 "https://example.com/avatar1.jpg");
        
        // Serialize to JSON
        String json = JsonUtils.toJson(user);
        assertThat(json, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(json), is(true));
        
        // Verify JSON contains expected fields
        assertThat(json, containsString("John Doe"));
        assertThat(json, containsString("Software Engineer"));
        assertThat(json, containsString("john.doe@example.com"));
        
        // Pretty print JSON
        String prettyJson = JsonUtils.toPrettyJson(user);
        assertThat(prettyJson, is(not(emptyString())));
        assertThat(prettyJson, containsString("\n")); // Should contain newlines for pretty printing
    }

    @Test
    public void testUserDeserialization() {
        // Create JSON string
        String json = """
                {
                    "id": 2,
                    "name": "Jane Smith",
                    "job": "QA Engineer",
                    "email": "jane.smith@example.com",
                    "first_name": "Jane",
                    "last_name": "Smith",
                    "avatar": "https://example.com/avatar2.jpg"
                }
                """;
        
        // Deserialize to User object
        User user = JsonUtils.fromJson(json, User.class);
        
        // Verify all fields are correctly deserialized
        assertThat(user.getId(), equalTo(2));
        assertThat(user.getName(), equalTo("Jane Smith"));
        assertThat(user.getJob(), equalTo("QA Engineer"));
        assertThat(user.getEmail(), equalTo("jane.smith@example.com"));
        assertThat(user.getFirstName(), equalTo("Jane"));
        assertThat(user.getLastName(), equalTo("Smith"));
        assertThat(user.getAvatar(), equalTo("https://example.com/avatar2.jpg"));
    }

    @Test
    public void testApiResponseSerialization() {
        // Create Support object
        Support support = TestDataBuilder.buildDefaultSupport();
        
        // Create User object
        User user = TestDataBuilder.buildUser("Bob Johnson", "DevOps Engineer");
        
        // Create ApiResponse object
        ApiResponse<User> apiResponse = new ApiResponse<>(user, support);
        
        // Serialize to JSON
        String json = JsonUtils.toJson(apiResponse);
        assertThat(json, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(json), is(true));
        
        // Verify JSON structure
        assertThat(json, containsString("data"));
        assertThat(json, containsString("support"));
        assertThat(json, containsString("Bob Johnson"));
        assertThat(json, containsString("DevOps Engineer"));
    }

    @Test
    public void testListSerialization() {
        // Create list of users
        List<User> users = TestDataBuilder.buildRandomUserList(3);
        
        // Serialize list to JSON
        String json = JsonUtils.toJson(users);
        assertThat(json, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(json), is(true));
        
        // Verify it's an array
        assertThat(json, startsWith("["));
        assertThat(json, endsWith("]"));
        
        // Deserialize back to list
        List<User> deserializedUsers = JsonUtils.fromJsonToList(json, User.class);
        assertThat(deserializedUsers, hasSize(3));
        assertThat(deserializedUsers, equalTo(users));
    }

    @Test
    public void testMapConversion() {
        // Create User object
        User user = TestDataBuilder.buildUser("Alice Brown", "Product Manager");
        
        // Convert to Map
        Map<String, Object> userMap = JsonUtils.toMap(user);
        
        // Verify map contains expected keys and values
        assertThat(userMap, hasKey("name"));
        assertThat(userMap, hasKey("job"));
        assertThat(userMap.get("name"), equalTo("Alice Brown"));
        assertThat(userMap.get("job"), equalTo("Product Manager"));
        
        // Convert Map back to User object
        User convertedUser = JsonUtils.fromMap(userMap, User.class);
        assertThat(convertedUser, equalTo(user));
    }

    @Test
    public void testDeepCopy() {
        // Create original User object
        User originalUser = TestDataBuilder.buildFullUser(5, "Charlie Wilson", "Data Analyst",
                                                        "charlie.wilson@example.com", "Charlie", "Wilson",
                                                        "https://example.com/avatar5.jpg");
        
        // Create deep copy
        User copiedUser = JsonUtils.deepCopy(originalUser, User.class);
        
        // Verify they are equal but not the same instance
        assertThat(copiedUser, equalTo(originalUser));
        assertThat(copiedUser, is(not(sameInstance(originalUser))));
        
        // Modify copied user
        copiedUser.setName("Modified Name");
        
        // Verify original is unchanged
        assertThat(originalUser.getName(), equalTo("Charlie Wilson"));
        assertThat(copiedUser.getName(), equalTo("Modified Name"));
    }

    @Test
    public void testJsonNodeExtraction() {
        // Create complex JSON
        String json = """
                {
                    "data": {
                        "user": {
                            "id": 10,
                            "name": "Test User",
                            "details": {
                                "age": 30,
                                "city": "New York"
                            }
                        }
                    },
                    "status": "success"
                }
                """;
        
        // Extract specific nodes
        String userName = JsonUtils.getJsonNodeAsString(json, "/data/user/name");
        String userAge = JsonUtils.getJsonNodeAsString(json, "/data/user/details/age");
        String status = JsonUtils.getJsonNodeAsString(json, "/status");
        String nonExistent = JsonUtils.getJsonNodeAsString(json, "/non/existent");
        
        // Verify extracted values
        assertThat(userName, equalTo("Test User"));
        assertThat(userAge, equalTo("30"));
        assertThat(status, equalTo("success"));
        assertThat(nonExistent, is(nullValue()));
    }

    @Test
    public void testJsonValidation() {
        // Valid JSON
        String validJson = "{\"name\": \"Test\", \"value\": 123}";
        assertThat(JsonUtils.isValidJson(validJson), is(true));
        
        // Invalid JSON
        String invalidJson = "{\"name\": \"Test\", \"value\": 123,";
        assertThat(JsonUtils.isValidJson(invalidJson), is(false));
        
        // Empty string
        assertThat(JsonUtils.isValidJson(""), is(false));
        
        // Null
        assertThat(JsonUtils.isValidJson(null), is(false));
    }

    @Test
    public void testJsonMerging() {
        String json1 = "{\"name\": \"John\", \"age\": 30}";
        String json2 = "{\"city\": \"New York\", \"job\": \"Engineer\"}";
        
        String merged = JsonUtils.mergeJson(json1, json2);
        assertThat(merged, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(merged), is(true));
        
        // Verify all fields are present
        assertThat(merged, containsString("John"));
        assertThat(merged, containsString("30"));
        assertThat(merged, containsString("New York"));
        assertThat(merged, containsString("Engineer"));
    }
}
