# Advanced Framework Implementation Guide

This document provides detailed notes on how the advanced concepts have been implemented in the test automation framework.

## Table of Contents

1. [Dynamic Payloads with Builder Pattern](#dynamic-payloads-with-builder-pattern)
2. [Faker Library for Realistic Test Data](#faker-library-for-realistic-test-data)
3. [Centralized Test Data Management](#centralized-test-data-management)
4. [Complex JSON Schema Validation](#complex-json-schema-validation)
5. [Excel/JSON Test Data Integration](#exceljson-test-data-integration)
6. [JWT Token Management](#jwt-token-management)
7. [Design Patterns Implementation](#design-patterns-implementation)
8. [Mock Server with WireMock](#mock-server-with-wiremock)
9. [Service Layer Abstraction](#service-layer-abstraction)
10. [Framework Integration](#framework-integration)

## 1. Dynamic Payloads with Builder Pattern

### Implementation Details

**File**: `src/test/java/com/hemanth/core/PayloadBuilder.java`

The Builder Pattern has been implemented to create dynamic payloads with the following features:

#### Key Components:

1. **Main PayloadBuilder Class**
   - Generic payload builder for custom key-value pairs
   - Methods: `addField()`, `removeField()`, `clear()`, `build()`

2. **Specialized Builders**
   - `UserPayloadBuilder`: For user-related payloads
   - `LoginPayloadBuilder`: For authentication payloads
   - `ProductPayloadBuilder`: For product-related payloads
   - `OrderPayloadBuilder`: For order-related payloads

#### Usage Examples:

```java
// Build user payload with specific data
Map<String, Object> userPayload = PayloadBuilder.user()
    .withName("John Doe")
    .withJob("Software Engineer")
    .withEmail("john@example.com")
    .build();

// Build user payload with random data
Map<String, Object> randomUser = PayloadBuilder.user()
    .withRandomData()
    .build();

// Build custom payload
Map<String, Object> customPayload = PayloadBuilder.custom()
    .addField("key1", "value1")
    .addField("key2", "value2")
    .build();
```

#### Benefits:
- **Fluent Interface**: Method chaining for easy payload construction
- **Type Safety**: Compile-time validation of payload structure
- **Reusability**: Consistent payload creation across tests
- **Maintainability**: Easy to modify and extend

## 2. Faker Library for Realistic Test Data

### Implementation Details

**Dependency**: `com.github.javafaker:faker:1.0.2`

The Faker library has been integrated to generate realistic test data:

#### Features:
- **Realistic Names**: Full names, first names, last names
- **Job Titles**: Professional job descriptions
- **Email Addresses**: Valid email formats
- **Addresses**: Street addresses, cities, countries
- **Phone Numbers**: Valid phone number formats
- **Dates**: Birth dates, creation dates

#### Usage Examples:

```java
Faker faker = new Faker();

// Generate realistic user data
String fullName = faker.name().fullName();
String jobTitle = faker.job().title();
String email = faker.internet().emailAddress();
String address = faker.address().fullAddress();
```

#### Benefits:
- **Realistic Data**: More authentic test scenarios
- **Variety**: Different data for each test run
- **Maintenance**: No need to manually update test data
- **Coverage**: Tests edge cases with diverse data

## 3. Centralized Test Data Management

### Implementation Details

**File**: `src/test/java/com/hemanth/util/TestDataManager.java`

A comprehensive test data management system has been implemented:

#### Key Features:

1. **Excel File Operations**
   - Read Excel files (.xlsx format)
   - Extract data from specific sheets
   - Support for multiple data types (String, Number, Date, Boolean)

2. **JSON File Operations**
   - Read JSON files from file system
   - Read JSON files from classpath
   - Deserialize to POJO objects

3. **Caching Mechanism**
   - In-memory caching for performance
   - Cache invalidation and management
   - Memory-efficient data storage

4. **Data Filtering and Selection**
   - Filter by column values
   - Random data selection
   - Scenario-based data retrieval

#### Usage Examples:

```java
TestDataManager tdm = TestDataManager.getInstance();

// Read Excel data
List<Map<String, Object>> userData = tdm.getTestDataFromExcel(
    "testdata/users.xlsx", "Users");

// Get data by condition
List<Map<String, Object>> adminUsers = tdm.getTestDataByCondition(
    "testdata/users.xlsx", "Users", "role", "admin");

// Get random test data
Map<String, Object> randomUser = tdm.getRandomTestData(
    "testdata/users.xlsx", "Users");
```

#### Benefits:
- **Centralized Management**: Single source of truth for test data
- **Performance**: Caching reduces file I/O operations
- **Flexibility**: Support for multiple data formats
- **Maintainability**: Easy to update and manage test data

## 4. Complex JSON Schema Validation

### Implementation Details

**File**: `src/test/java/com/hemanth/util/JsonUtils.java`

Enhanced JSON utilities for complex schema validation:

#### Key Features:

1. **Schema Validation**
   - JSON syntax validation
   - Structure validation
   - Type checking

2. **Node Extraction**
   - JSON path-based data extraction
   - Nested object navigation
   - Array element access

3. **Advanced Operations**
   - JSON merging
   - Deep copying
   - Transformation utilities

#### Usage Examples:

```java
// Validate JSON syntax
boolean isValid = JsonUtils.isValidJson(jsonString);

// Extract specific nodes
String userName = JsonUtils.getJsonNodeAsString(json, "/data/user/name");
String userAge = JsonUtils.getJsonNodeAsString(json, "/data/user/details/age");

// Merge JSON objects
String merged = JsonUtils.mergeJson(json1, json2);

// Deep copy objects
User copiedUser = JsonUtils.deepCopy(originalUser, User.class);
```

#### Benefits:
- **Robust Validation**: Comprehensive JSON validation
- **Flexible Access**: Easy data extraction from complex structures
- **Data Integrity**: Ensures data consistency and validity
- **Debugging**: Better error messages and validation feedback

## 5. Excel/JSON Test Data Integration

### Implementation Details

**Dependencies**: 
- `org.apache.poi:poi:5.2.5`
- `org.apache.poi:poi-ooxml:5.2.5`

#### Excel Integration Features:

1. **File Reading**
   - Support for .xlsx format
   - Multiple sheet handling
   - Header row detection

2. **Data Extraction**
   - Cell type detection (String, Number, Date, Boolean)
   - Formula evaluation
   - Empty cell handling

3. **Data Writing**
   - Create new Excel files
   - Update existing files
   - Auto-column sizing

#### JSON Integration Features:

1. **File Operations**
   - Read from file system
   - Read from classpath resources
   - Write to files

2. **Data Conversion**
   - POJO serialization/deserialization
   - Type-safe data handling
   - Error handling and validation

#### Usage Examples:

```java
// Excel operations
List<Map<String, Object>> excelData = tdm.readExcelFile("data.xlsx", "Sheet1");
tdm.writeTestDataToExcel("output.xlsx", "Results", testResults);

// JSON operations
User user = tdm.readJsonFile("user.json", User.class);
List<User> users = tdm.readJsonFromClasspath("users.json", List.class);
```

## 6. JWT Token Management

### Implementation Details

**File**: `src/test/java/com/hemanth/core/TokenManager.java`
**Dependencies**: `io.jsonwebtoken:jjwt-api:0.12.5`

#### Key Features:

1. **Token Generation**
   - JWT token creation with custom claims
   - Configurable expiration times
   - Role-based access control

2. **Token Validation**
   - Signature verification
   - Expiration checking
   - Claim extraction

3. **Token Renewal**
   - Automatic expiration detection
   - Token refresh logic
   - Renewal threshold configuration

4. **Security Features**
   - HMAC-SHA256 signing
   - Configurable secrets
   - Secure token storage

#### Usage Examples:

```java
TokenManager tm = TokenManager.getInstance();

// Generate token
String token = tm.generateToken("username", "admin");

// Validate token
boolean isValid = tm.validateToken(token);

// Check expiration
boolean isExpired = tm.isTokenExpired(token);

// Renew token
if (tm.needsRenewal()) {
    String newToken = tm.renewToken();
}

// Get authorization header
String authHeader = tm.getAuthorizationHeader();
```

#### Benefits:
- **Security**: JWT-based authentication
- **Flexibility**: Custom claims and roles
- **Automation**: Automatic token management
- **Integration**: Seamless API authentication

## 7. Design Patterns Implementation

### 7.1 Builder Pattern (Payloads)

**Implementation**: `PayloadBuilder.java`

- **Purpose**: Create complex objects step by step
- **Benefits**: Readable code, flexible object creation
- **Usage**: Dynamic payload building for API requests

### 7.2 Factory Pattern (Request Objects)

**Implementation**: `RequestFactory.java`

- **Purpose**: Create different types of request objects
- **Benefits**: Encapsulated object creation, easy extension
- **Usage**: HTTP request creation with different configurations

### 7.3 Singleton (Config Management)

**Implementation**: `ConfigManager.java`

- **Purpose**: Ensure single instance of configuration
- **Benefits**: Centralized configuration, memory efficiency
- **Usage**: Application-wide configuration management

## 8. Mock Server with WireMock

### Implementation Details

**File**: `src/test/java/com/hemanth/mock/MockServer.java`
**Dependency**: `org.wiremock:wiremock-standalone:3.4.2`

#### Key Features:

1. **Server Management**
   - Start/stop mock server
   - Port configuration
   - Server status monitoring

2. **Stubbing Capabilities**
   - HTTP method stubbing (GET, POST, PUT, DELETE)
   - Response customization
   - Status code configuration

3. **Advanced Stubbing**
   - Request body matching
   - Query parameter stubbing
   - Header-based stubbing

4. **Response Simulation**
   - Delay simulation (slow responses)
   - Fault simulation (failed responses)
   - Response transformation

#### Usage Examples:

```java
MockServer mock = MockServer.getInstance();

// Start server
mock.start();

// Basic stubbing
mock.stubGet("/api/users", 200, "{\"users\": []}");

// Stub with delay
mock.stubWithDelay("GET", "/api/slow", 200, "{\"status\": \"delayed\"}", 2000);

// Stub with fault
mock.stubWithFault("GET", "/api/error", "connection_reset");

// Verify requests
mock.verifyRequest("GET", "/api/users");
```

#### Benefits:
- **Dependency Isolation**: Test without external services
- **Response Control**: Simulate various scenarios
- **Performance Testing**: Simulate slow responses
- **Error Testing**: Simulate failure conditions

## 9. Service Layer Abstraction

### Implementation Details

**File**: `src/test/java/com/hemanth/services/UserService.java`

#### Key Features:

1. **API Abstraction**
   - High-level API operations
   - Request/response handling
   - Error management

2. **Data Validation**
   - Input validation
   - Response validation
   - Business rule enforcement

3. **Multiple Data Formats**
   - POJO objects
   - JSON strings
   - Map objects

#### Usage Examples:

```java
UserService userService = new UserService();

// Create user with POJO
User user = new User("John", "Engineer");
Response response = userService.createUser(user);

// Create user with Map
Map<String, Object> userData = Map.of("name", "Jane", "job", "Manager");
Response response2 = userService.createUser(userData);

// Get user by ID
Response userResponse = userService.getUserById(1);

// Update user
Response updateResponse = userService.updateUser(1, updateData);
```

#### Benefits:
- **Abstraction**: Hide implementation details
- **Reusability**: Consistent API usage across tests
- **Maintainability**: Centralized API logic
- **Testing**: Easier to mock and test

## 10. Framework Integration

### Configuration Management

**File**: `src/test/java/com/hemanth/config/ConfigManager.java`

- **Properties File**: `config/qa.properties`
- **Configuration**: Base URLs, timeouts, JWT settings
- **Environment**: Support for different environments

### Dependencies Management

**File**: `pom.xml`

- **Core Dependencies**: RestAssured, TestNG, Jackson
- **Advanced Dependencies**: JWT, WireMock, Apache POI
- **Version Management**: Centralized version control

### Test Execution

**File**: `testng.xml`

- **Test Discovery**: Automatic test class discovery
- **Parallel Execution**: Configurable parallel test execution
- **Environment Variables**: System property configuration

## Running the Advanced Framework

### Prerequisites

1. **Java 21**: Required for framework execution
2. **Maven**: For dependency management and build
3. **Test Data**: Excel/JSON files in testdata directory

### Execution Commands

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AdvancedFrameworkTest

# Run with specific environment
mvn test -Denv=qa

# Run with parallel execution
mvn test -Dparallel=true
```

### Configuration

1. **Environment Setup**: Configure `config/qa.properties`
2. **Mock Server**: Enable/disable in configuration
3. **Test Data**: Place Excel/JSON files in testdata directory
4. **JWT Configuration**: Set JWT secret and expiration

## Best Practices

### 1. Test Data Management
- Use descriptive sheet names and column headers
- Implement data versioning and backup
- Regular data validation and cleanup

### 2. Mock Server Usage
- Start server in @BeforeClass
- Reset stubs in @BeforeMethod
- Stop server in @AfterClass
- Use meaningful stub descriptions

### 3. Token Management
- Implement proper token expiration handling
- Use secure JWT secrets
- Implement token refresh logic
- Clear tokens after tests

### 4. Error Handling
- Implement comprehensive error handling
- Use meaningful error messages
- Log errors for debugging
- Implement retry mechanisms

## Future Enhancements

### 1. Advanced Mocking
- Response templating
- Dynamic response generation
- Request/response recording
- Mock server clustering

### 2. Enhanced Data Management
- Database integration
- API-based test data
- Data generation algorithms
- Data validation rules

### 3. Performance Testing
- Load testing integration
- Response time monitoring
- Performance metrics collection
- Benchmarking tools

### 4. Security Testing
- OAuth 2.0 integration
- API security testing
- Vulnerability scanning
- Security compliance testing

## Conclusion

The advanced framework implementation provides:

- **Comprehensive Testing**: Full-featured test automation framework
- **Design Patterns**: Industry-standard software design patterns
- **Mocking Capabilities**: Advanced service mocking and simulation
- **Data Management**: Flexible test data handling
- **Security**: JWT-based authentication and authorization
- **Maintainability**: Clean, organized, and extensible code
- **Performance**: Efficient caching and resource management
- **Integration**: Seamless integration with existing tools

This framework serves as a solid foundation for enterprise-level test automation with room for future enhancements and customization.
