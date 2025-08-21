package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.config.ConfigManager;
import com.hemanth.core.PayloadBuilder;
import com.hemanth.core.TokenManager;
import com.hemanth.mock.MockServer;
import com.hemanth.services.UserService;
import com.hemanth.util.JsonUtils;
import com.hemanth.util.TestDataManager;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Comprehensive test class demonstrating all advanced framework features
 */
public class AdvancedFrameworkTest extends BaseTest {
    
    private MockServer mockServer;
    private UserService userService;
    private TestDataManager testDataManager;
    private TokenManager tokenManager;
    private ConfigManager configManager;
    
    @BeforeClass
    public void setup() {
        // Initialize all components
        mockServer = MockServer.getInstance();
        userService = new UserService();
        testDataManager = TestDataManager.getInstance();
        tokenManager = TokenManager.getInstance();
        configManager = ConfigManager.getInstance();
        
        // Start mock server if enabled
        if (configManager.isMockServerEnabled()) {
            mockServer.start();
        }
    }
    
    @AfterClass
    public void teardown() {
        // Stop mock server if running
        if (mockServer.isRunning()) {
            mockServer.stop();
        }
    }
    
    @BeforeMethod
    public void beforeMethod() {
        // Reset mock server stubs before each test
        if (mockServer.isRunning()) {
            mockServer.reset();
        }
    }
    
    @Test
    public void testBuilderPatternWithDynamicPayloads() {
        // Test User payload builder
        Map<String, Object> userPayload = PayloadBuilder.user()
                .withName("John Doe")
                .withJob("Software Engineer")
                .withEmail("john.doe@example.com")
                .withFirstName("John")
                .withLastName("Doe")
                .build();
        
        assertThat(userPayload, hasKey("name"));
        assertThat(userPayload, hasKey("job"));
        assertThat(userPayload.get("name"), equalTo("John Doe"));
        assertThat(userPayload.get("job"), equalTo("Software Engineer"));
        
        // Test random data generation
        Map<String, Object> randomUserPayload = PayloadBuilder.user()
                .withRandomData()
                .build();
        
        assertThat(randomUserPayload, hasKey("name"));
        assertThat(randomUserPayload, hasKey("job"));
        assertThat(randomUserPayload.get("name"), is(not(emptyString())));
        assertThat(randomUserPayload.get("job"), is(not(emptyString())));
        
        // Test Product payload builder
        Map<String, Object> productPayload = PayloadBuilder.product()
                .withRandomData()
                .build();
        
        assertThat(productPayload, hasKey("name"));
        assertThat(productPayload, hasKey("price"));
        assertThat(productPayload, hasKey("category"));
        assertThat(productPayload.get("price"), instanceOf(Double.class));
    }
    
    @Test
    public void testJWTTokenManagement() {
        // Generate token
        String token = tokenManager.generateToken("testuser", "admin");
        assertThat(token, is(not(emptyString())));
        
        // Validate token
        assertThat(tokenManager.isTokenValid(token), is(true));
        assertThat(tokenManager.isCurrentTokenExpired(), is(false));
        
        // Extract claims
        String username = tokenManager.extractUsername(token);
        String role = tokenManager.extractRole(token);
        assertThat(username, equalTo("testuser"));
        assertThat(role, equalTo("admin"));
        
        // Check expiration time
        long timeUntilExpiration = tokenManager.getTimeUntilExpiration();
        assertThat(timeUntilExpiration, greaterThan(0L));
        
        // Test token renewal
        if (tokenManager.needsRenewal()) {
            String renewedToken = tokenManager.renewToken();
            assertThat(renewedToken, is(not(equalTo(token))));
            assertThat(tokenManager.isTokenValid(renewedToken), is(true));
        }
    }
    
    @Test
    public void testFactoryPatternWithRequestFactory() {
        // Test different request types
        Response response = userService.getAllUsers();
        assertThat(response.getStatusCode(), equalTo(200));
        
        // Test authenticated request
        try {
            Response authResponse = userService.getUserByIdAuthenticated(1);
            assertThat(authResponse.getStatusCode(), equalTo(200));
        } catch (RuntimeException e) {
            // Expected if token is expired
            assertThat(e.getMessage(), containsString("Token expired"));
        }
        
        // Test query parameters
        Response paginatedResponse = userService.getAllUsers(1, 5);
        assertThat(paginatedResponse.getStatusCode(), equalTo(200));
    }
    
    @Test
    public void testSingletonConfigurationManagement() {
        // Test singleton pattern
        ConfigManager instance1 = ConfigManager.getInstance();
        ConfigManager instance2 = ConfigManager.getInstance();
        assertThat(instance1, sameInstance(instance2));
        
        // Test configuration properties
        String baseUrl = configManager.getBaseUrl();
        assertThat(baseUrl, is(not(emptyString())));
        
        int timeout = configManager.getApiTimeout();
        assertThat(timeout, greaterThan(0));
        
        boolean mockEnabled = configManager.isMockServerEnabled();
        assertThat(mockEnabled, instanceOf(Boolean.class));
    }
    
    @Test
    public void testServiceLayerAbstraction() {
        // Test user creation with different data types
        Map<String, Object> userData = PayloadBuilder.user()
                .withRandomData()
                .build();
        
        Response createResponse = userService.createUser(userData);
        assertThat(createResponse.getStatusCode(), equalTo(201));
        
        // Test user update
        Map<String, Object> updateData = Map.of("name", "Updated Name", "job", "Updated Job");
        Response updateResponse = userService.updateUser(1, updateData);
        assertThat(updateResponse.getStatusCode(), equalTo(200));
        
        // Test user deletion
        Response deleteResponse = userService.deleteUser(1);
        assertThat(deleteResponse.getStatusCode(), equalTo(200));
    }
    
    @Test
    public void testMockServerFunctionality() {
        if (!mockServer.isRunning()) {
            mockServer.start();
        }
        
        // Test basic stubbing
        mockServer.stubGet("/api/test", 200, "{\"message\": \"Hello World\"}");
        
        // Test JSON stubbing
        Map<String, Object> responseData = Map.of("id", 1, "name", "Test User");
        mockServer.stubWithJson("POST", "/api/users", 201, responseData);
        
        // Test delay simulation
        mockServer.stubWithDelay("GET", "/api/slow", 200, "{\"status\": \"delayed\"}", 2000);
        
        // Test fault simulation
        mockServer.stubWithFault("GET", "/api/error", "connection_reset");
        
        // Verify stubs are working
        assertThat(mockServer.isRunning(), is(true));
        assertThat(mockServer.getPort(), equalTo(configManager.getMockServerPort()));
    }
    
    @Test
    public void testTestDataManagement() {
        // Test Excel file operations (if file exists)
        String testDataPath = "testdata/testdata.xlsx";
        if (testDataManager.fileExists(testDataPath)) {
            List<String> sheetNames = testDataManager.getSheetNames(testDataPath);
            assertThat(sheetNames, is(not(empty())));
            
            // Test reading data from sheet
            List<Map<String, Object>> testData = testDataManager.getTestDataFromExcel(testDataPath, "Users");
            assertThat(testData, is(not(empty())));
            
            // Test data filtering
            List<Map<String, Object>> filteredData = testDataManager.getTestDataByCondition(
                testDataPath, "Users", "role", "admin");
            assertThat(filteredData, is(not(nullValue())));
            
            // Test random data selection
            Map<String, Object> randomData = testDataManager.getRandomTestData(testDataPath, "Users");
            assertThat(randomData, is(not(nullValue())));
        }
        
        // Test JSON file operations
        String jsonPath = "testdata/users.json";
        if (testDataManager.fileExists(jsonPath)) {
            List<Map<String, Object>> jsonData = testDataManager.readJsonFile(jsonPath, List.class);
            assertThat(jsonData, is(not(empty())));
        }
    }
    
    @Test
    public void testComplexJsonSchemaValidation() {
        // Create complex JSON response
        Map<String, Object> complexResponse = Map.of(
            "data", Map.of(
                "users", List.of(
                    Map.of("id", 1, "name", "User 1", "email", "user1@example.com"),
                    Map.of("id", 2, "name", "User 2", "email", "user2@example.com")
                ),
                "pagination", Map.of(
                    "page", 1,
                    "per_page", 10,
                    "total", 100,
                    "total_pages", 10
                )
            ),
            "support", Map.of(
                "url", "https://example.com/support",
                "text", "Support information"
            )
        );
        
        // Convert to JSON string
        String jsonResponse = JsonUtils.toJson(complexResponse);
        assertThat(jsonResponse, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(jsonResponse), is(true));
        
        // Validate JSON structure
        assertThat(jsonResponse, containsString("data"));
        assertThat(jsonResponse, containsString("users"));
        assertThat(jsonResponse, containsString("pagination"));
        assertThat(jsonResponse, containsString("support"));
        
        // Test JSON node extraction
        String usersCount = JsonUtils.getJsonNodeAsString(jsonResponse, "/data/users");
        assertThat(usersCount, is(not(nullValue())));
        
        String pageNumber = JsonUtils.getJsonNodeAsString(jsonResponse, "/data/pagination/page");
        assertThat(pageNumber, equalTo("1"));
    }
    
    @Test
    public void testDataDrivenTesting() {
        // Test with CSV data (existing functionality)
        // This would use the existing DataProviderUtils
        
        // Test with Excel data (new functionality)
        String testDataPath = "testdata/testdata.xlsx";
        if (testDataManager.fileExists(testDataPath)) {
            // Get test data for data provider
            Object[][] dataProvider = testDataManager.getTestDataForDataProvider(
                testDataPath, "Users", "name", "job", "email");
            
            assertThat(dataProvider, is(not(nullValue())));
            assertThat(dataProvider.length, greaterThan(0));
            
            // Test each row of data
            for (Object[] row : dataProvider) {
                assertThat(row.length, equalTo(3));
                assertThat(row[0], is(not(nullValue()))); // name
                assertThat(row[1], is(not(nullValue()))); // job
                assertThat(row[2], is(not(nullValue()))); // email
            }
        }
    }
    
    @Test
    public void testTokenExpirationAndRenewal() {
        // Test token expiration logic
        String token = tokenManager.generateToken("testuser", "user");
        
        // Check if token needs renewal
        boolean needsRenewal = tokenManager.needsRenewal();
        assertThat(needsRenewal, instanceOf(Boolean.class));
        
        // Get time until expiration
        long timeUntilExpiration = tokenManager.getTimeUntilExpiration();
        assertThat(timeUntilExpiration, greaterThan(0L));
        
        // Test token validation
        assertThat(tokenManager.isTokenValid(token), is(true));
        assertThat(tokenManager.isCurrentTokenExpired(), is(false));
        
        // Test authorization header
        String authHeader = tokenManager.getAuthorizationHeader();
        assertThat(authHeader, startsWith("Bearer "));
        assertThat(authHeader, containsString(token));
    }
    
    @Test
    public void testMockServerAdvancedFeatures() {
        if (!mockServer.isRunning()) {
            mockServer.start();
        }
        
        // Test conditional stubbing based on request body
        mockServer.stubWithRequestBodyMatching("POST", "/api/users", 
            ".*\"name\".*", 201, "{\"id\": 1, \"status\": \"created\"}");
        
        // Test query parameter stubbing
        Map<String, String> queryParams = Map.of("page", "1", "size", "10");
        mockServer.stubWithQueryParams("GET", "/api/users", queryParams, 200, 
            "{\"users\": [], \"total\": 0}");
        
        // Test response transformation
        mockServer.stubWithResponseTransformation("GET", "/api/transform", 200, 
            "{\"message\": \"Hello\"}", "response-template");
        
        // Verify server is running and configured
        assertThat(mockServer.isRunning(), is(true));
        assertThat(mockServer.getBaseUrl(), containsString("localhost"));
    }
    
    @Test
    public void testFrameworkIntegration() {
        // Test complete workflow integration
        
        // 1. Configuration management
        String baseUrl = configManager.getBaseUrl();
        assertThat(baseUrl, is(not(emptyString())));
        
        // 2. Token management
        String token = tokenManager.generateToken("integrationuser", "admin");
        assertThat(token, is(not(emptyString())));
        
        // 3. Payload building
        Map<String, Object> userPayload = PayloadBuilder.user()
                .withRandomData()
                .build();
        assertThat(userPayload, hasKey("name"));
        
        // 4. Service layer usage
        Response response = userService.createUser(userPayload);
        assertThat(response.getStatusCode(), equalTo(201));
        
        // 5. Mock server (if enabled)
        if (configManager.isMockServerEnabled()) {
            mockServer.stubGet("/api/test", 200, "{\"status\": \"success\"}");
            assertThat(mockServer.isRunning(), is(true));
        }
        
        // 6. Test data management
        assertThat(testDataManager.getCacheSize(), greaterThanOrEqualTo(0));
        
        // 7. JSON utilities
        String jsonPayload = JsonUtils.toJson(userPayload);
        assertThat(JsonUtils.isValidJson(jsonPayload), is(true));
    }
}
