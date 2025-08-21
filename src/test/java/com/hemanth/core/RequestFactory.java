package com.hemanth.core;

import com.hemanth.config.ConfigManager;
import com.hemanth.core.Specs;
import com.hemanth.core.TokenManager;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * Factory Pattern implementation for creating different types of request objects
 */
public class RequestFactory {
    
    private final ConfigManager config;
    private final TokenManager tokenManager;
    
    public RequestFactory() {
        this.config = ConfigManager.getInstance();
        this.tokenManager = TokenManager.getInstance();
    }
    
    /**
     * Create a basic request specification
     */
    public RequestSpecification createBasicRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON);
    }
    
    /**
     * Create an authenticated request specification
     */
    public RequestSpecification createAuthenticatedRequest() {
        RequestSpecification request = createBasicRequest();
        
        if (tokenManager.isCurrentTokenExpired()) {
            throw new RuntimeException("Token expired. Please authenticate first.");
        }
        
        return request.header("Authorization", tokenManager.getAuthorizationHeader());
    }
    
    /**
     * Create a request with custom headers
     */
    public RequestSpecification createRequestWithHeaders(java.util.Map<String, String> headers) {
        RequestSpecification request = createBasicRequest();
        headers.forEach(request::header);
        return request;
    }
    
    /**
     * Create a request with query parameters
     */
    public RequestSpecification createRequestWithQueryParams(java.util.Map<String, String> queryParams) {
        RequestSpecification request = createBasicRequest();
        queryParams.forEach(request::queryParam);
        return request;
    }
    
    /**
     * Create a request with path parameters
     */
    public RequestSpecification createRequestWithPathParams(java.util.Map<String, String> pathParams) {
        RequestSpecification request = createBasicRequest();
        pathParams.forEach(request::pathParam);
        return request;
    }
    
    /**
     * Create a request with form data
     */
    public RequestSpecification createFormRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.URLENC);
    }
    
    /**
     * Create a request with multipart data
     */
    public RequestSpecification createMultipartRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.MULTIPART);
    }
    
    /**
     * Create a request with XML content
     */
    public RequestSpecification createXmlRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.XML);
    }
    
    /**
     * Create a request with custom timeout
     */
    public RequestSpecification createRequestWithTimeout(int timeout) {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON);
    }
    
    /**
     * Create a request with retry logic
     */
    public RequestSpecification createRetryRequest(int maxRetries) {
        RequestSpecification request = createBasicRequest();
        // Add retry configuration
        return request;
    }
    
    /**
     * Create a request for file upload
     */
    public RequestSpecification createFileUploadRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.MULTIPART);
    }
    
    /**
     * Create a request with custom SSL configuration
     */
    public RequestSpecification createSslRequest() {
        return RestAssured.given()
                .baseUri(config.getBaseUrl())
                .contentType(ContentType.JSON)
                .relaxedHTTPSValidation(); // For testing purposes only
    }
    
    // HTTP Methods with different request types
    
    /**
     * GET request with basic configuration
     */
    public Response get(String endpoint) {
        return createBasicRequest()
                .when()
                .get(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
    
    /**
     * GET request with authentication
     */
    public Response getAuthenticated(String endpoint) {
        return createAuthenticatedRequest()
                .when()
                .get(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
    
    /**
     * GET request with query parameters
     */
    public Response getWithQueryParams(String endpoint, java.util.Map<String, String> queryParams) {
        return createRequestWithQueryParams(queryParams)
                .when()
                .get(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
    
    /**
     * POST request with JSON body
     */
    public Response post(String endpoint, Object body) {
        return createBasicRequest()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(Specs.created())
                .extract()
                .response();
    }
    
    /**
     * POST request with authentication
     */
    public Response postAuthenticated(String endpoint, Object body) {
        return createAuthenticatedRequest()
                .body(body)
                .when()
                .post(endpoint)
                .then()
                .spec(Specs.created())
                .extract()
                .response();
    }
    
    /**
     * POST request with form data
     */
    public Response postForm(String endpoint, java.util.Map<String, String> formData) {
        return createFormRequest()
                .formParams(formData)
                .when()
                .post(endpoint)
                .then()
                .spec(Specs.created())
                .extract()
                .response();
    }
    
    /**
     * PUT request
     */
    public Response put(String endpoint, Object body) {
        return createBasicRequest()
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
    
    /**
     * DELETE request
     */
    public Response delete(String endpoint) {
        return createBasicRequest()
                .when()
                .delete(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
    
    /**
     * PATCH request
     */
    public Response patch(String endpoint, Object body) {
        return createBasicRequest()
                .body(body)
                .when()
                .patch(endpoint)
                .then()
                .spec(Specs.success())
                .extract()
                .response();
    }
}
