# POJO Implementation Guide

This document describes the implementation of POJO (Plain Old Java Object) classes, serialization, and deserialization in the test automation framework.

## Overview

The framework now includes a comprehensive POJO system that provides:
- Type-safe data models
- Automatic JSON serialization/deserialization
- Test data builders
- Utility classes for JSON operations

## POJO Classes

### 1. User Model (`src/test/java/com/hemanth/models/User.java`)

The `User` class represents user data with the following features:

- **Jackson Annotations**: Uses `@JsonProperty`, `@JsonInclude`, and `@JsonIgnoreProperties`
- **Builder Pattern**: Fluent setter methods that return `this` for method chaining
- **Multiple Constructors**: Default, minimal, and full constructors
- **Proper equals/hashCode/toString**: Overridden methods for object comparison and debugging

```java
User user = new User()
    .setName("John Doe")
    .setJob("Software Engineer")
    .setEmail("john.doe@example.com");
```

### 2. ApiResponse Model (`src/test/java/com/hemanth/models/ApiResponse.java`)

Generic wrapper class for API responses:

- **Generic Type**: `<T>` allows different data types
- **Multiple Response Types**: Handles single user, user list, and create/update responses
- **Support Information**: Includes support metadata for API responses

```java
ApiResponse<User> response = new ApiResponse<>(user, support);
```

### 3. Support Model (`src/test/java/com/hemanth/models/Support.java`)

Represents support information in API responses:

- **Simple Structure**: URL and text fields
- **Jackson Annotations**: Proper JSON mapping

## Serialization/Deserialization Utilities

### JsonUtils Class (`src/test/java/com/hemanth/util/JsonUtils.java`)

Comprehensive utility class for JSON operations:

#### Key Features:
- **ObjectMapper Configuration**: Pre-configured Jackson ObjectMapper
- **Serialization**: Convert objects to JSON strings
- **Deserialization**: Convert JSON to objects
- **File Operations**: Read/write JSON files
- **Validation**: JSON syntax validation
- **Advanced Operations**: Deep copy, JSON merging, node extraction

#### Usage Examples:

```java
// Serialize object to JSON
String json = JsonUtils.toJson(user);

// Deserialize JSON to object
User user = JsonUtils.fromJson(json, User.class);

// Deserialize to generic types
ApiResponse<User> response = JsonUtils.fromJson(json, 
    new TypeReference<ApiResponse<User>>() {});

// Validate JSON
boolean isValid = JsonUtils.isValidJson(json);

// Deep copy object
User copiedUser = JsonUtils.deepCopy(originalUser, User.class);
```

## Test Data Builders

### TestDataBuilder Class (`src/test/java/com/hemanth/util/TestDataBuilder.java`)

Utility class for creating test data objects:

#### Features:
- **Random Data Generation**: Creates objects with random values
- **Realistic Data**: Uses predefined names and job titles
- **Multiple Builders**: Different methods for different use cases
- **CSV Integration**: Builds objects from CSV data

#### Usage Examples:

```java
// Build random user
User randomUser = TestDataBuilder.buildRandomUser();

// Build user with specific data
User user = TestDataBuilder.buildUser("John Doe", "Engineer");

// Build realistic user
User realisticUser = TestDataBuilder.buildRealisticUser();

// Build list of users
List<User> users = TestDataBuilder.buildRandomUserList(5);
```

## Updated Test Classes

### 1. GetUserTest (`src/test/java/com/hemanth/tests/GetUserTest.java`)

Updated to use POJO classes:

- **POJO Deserialization**: Converts API response to `ApiResponse<User>`
- **Type-Safe Validation**: Uses User object properties instead of Map access
- **Serialization Testing**: Tests round-trip serialization/deserialization

### 2. CreateUserFromDataProviders (`src/test/java/com/hemanth/tests/CreateUserFromDataProviders.java`)

Enhanced with POJO capabilities:

- **TestDataBuilder Integration**: Uses builders for test data creation
- **POJO Serialization**: Converts User objects to JSON for requests
- **Response Validation**: Deserializes responses to POJO objects
- **Object Equality Testing**: Tests equals, hashCode, and deep copy

### 3. PojoSerializationTest (`src/test/java/com/hemanth/tests/PojoSerializationTest.java`)

New comprehensive test class demonstrating:

- **Serialization**: Object to JSON conversion
- **Deserialization**: JSON to object conversion
- **List Operations**: Serializing/deserializing collections
- **Map Conversion**: Object to Map and back
- **Deep Copy**: Object cloning via serialization
- **JSON Validation**: Syntax validation
- **Node Extraction**: JSON path operations
- **JSON Merging**: Combining multiple JSON objects

## Benefits of POJO Implementation

### 1. Type Safety
- Compile-time error checking
- IDE autocomplete and refactoring support
- Reduced runtime errors

### 2. Maintainability
- Centralized data models
- Easy to update when API changes
- Consistent data structure across tests

### 3. Readability
- Clear, self-documenting code
- Intuitive object creation and manipulation
- Better test assertions

### 4. Reusability
- Test data builders can be used across multiple tests
- Utility methods for common operations
- Consistent data creation patterns

### 5. Debugging
- Better error messages with object properties
- toString methods for logging
- IDE debugging support

## Best Practices

### 1. Model Design
- Use meaningful field names
- Include all necessary fields from API responses
- Use appropriate data types (Integer vs String for IDs)

### 2. Jackson Annotations
- Use `@JsonProperty` for field mapping
- Use `@JsonIgnoreProperties(ignoreUnknown = true)` for flexibility
- Use `@JsonInclude(JsonInclude.Include.NON_NULL)` to exclude null values

### 3. Builder Pattern
- Implement fluent setters for easy object creation
- Provide multiple constructors for different use cases
- Use TestDataBuilder for test data creation

### 4. Error Handling
- Wrap JSON operations in try-catch blocks
- Provide meaningful error messages
- Use runtime exceptions for unrecoverable errors

## Running the Tests

All tests can be run using the existing TestNG configuration:

```bash
mvn test
```

The framework will automatically discover and run:
- `GetUserTest`
- `CreateUserFromDataProviders`
- `PojoSerializationTest`

## Future Enhancements

Potential improvements for the POJO system:

1. **Custom Serializers**: For complex data types
2. **Validation Annotations**: Bean validation for data integrity
3. **Schema Generation**: Automatic JSON schema creation
4. **Data Factory**: More sophisticated test data generation
5. **Caching**: Object caching for performance
6. **Async Operations**: Background serialization/deserialization

## Conclusion

The POJO implementation provides a robust foundation for test automation with:
- Type-safe data handling
- Comprehensive serialization/deserialization
- Easy test data creation
- Maintainable and readable test code

This system makes the framework more professional, maintainable, and easier to extend with new API endpoints and data models.
