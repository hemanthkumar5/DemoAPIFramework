package com.hemanth.mock;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.hemanth.config.ConfigManager;
import com.hemanth.util.JsonUtils;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Mock Server using WireMock for stubbing unavailable dependencies and simulating responses
 */
public class MockServer {
    
    private static MockServer instance;
    private WireMockServer wireMockServer;
    private final ConfigManager config;
    
    private MockServer() {
        this.config = ConfigManager.getInstance();
    }
    
    /**
     * Get singleton instance
     */
    public static synchronized MockServer getInstance() {
        if (instance == null) {
            instance = new MockServer();
        }
        return instance;
    }
    
    /**
     * Start the mock server
     */
    public void start() {
        if (wireMockServer == null || !wireMockServer.isRunning()) {
            int port = config.getMockServerPort();
            wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().port(port));
            wireMockServer.start();
            WireMock.configureFor("localhost", port);
            System.out.println("Mock server started on port: " + port);
        }
    }
    
    /**
     * Stop the mock server
     */
    public void stop() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
            System.out.println("Mock server stopped");
        }
    }
    
    /**
     * Reset all stubs
     */
    public void reset() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            WireMock.reset();
            System.out.println("Mock server stubs reset");
        }
    }
    
    /**
     * Get mock server base URL
     */
    public String getBaseUrl() {
        return "http://localhost:" + config.getMockServerPort();
    }
    
    /**
     * Stub a GET endpoint
     */
    public void stubGet(String url, int statusCode, String responseBody) {
        stubFor(get(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
    
    /**
     * Stub a POST endpoint
     */
    public void stubPost(String url, int statusCode, String responseBody) {
        stubFor(post(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
    
    /**
     * Stub a PUT endpoint
     */
    public void stubPut(String url, int statusCode, String responseBody) {
        stubFor(put(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
    
    /**
     * Stub a DELETE endpoint
     */
    public void stubDelete(String url, int statusCode, String responseBody) {
        stubFor(delete(urlEqualTo(url))
                .willReturn(aResponse()
                        .withStatus(statusCode)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }
    
    /**
     * Stub with JSON response
     */
    public void stubWithJson(String method, String url, int statusCode, Object responseObject) {
        String responseBody = JsonUtils.toJson(responseObject);
        switch (method.toUpperCase()) {
            case "GET":
                stubGet(url, statusCode, responseBody);
                break;
            case "POST":
                stubPost(url, statusCode, responseBody);
                break;
            case "PUT":
                stubPut(url, statusCode, responseBody);
                break;
            case "DELETE":
                stubDelete(url, statusCode, responseBody);
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
    
    /**
     * Stub with delay (simulating slow responses)
     */
    public void stubWithDelay(String method, String url, int statusCode, String responseBody, int delayMs) {
        switch (method.toUpperCase()) {
            case "GET":
                stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                                .withFixedDelay(delayMs)));
                break;
            case "POST":
                stubFor(post(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                                .withFixedDelay(delayMs)));
                break;
            case "PUT":
                stubFor(put(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                                .withFixedDelay(delayMs)));
                break;
            case "DELETE":
                stubFor(delete(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                                .withFixedDelay(delayMs)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
    
    /**
     * Stub with fault (simulating failed responses)
     */
    public void stubWithFault(String method, String url, String faultType) {
        switch (method.toUpperCase()) {
            case "GET":
                stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse().withStatus(500).withBody("Simulated fault: " + faultType)));
                break;
            case "POST":
                stubFor(post(urlEqualTo(url))
                        .willReturn(aResponse().withStatus(500).withBody("Simulated fault: " + faultType)));
                break;
            case "PUT":
                stubFor(put(urlEqualTo(url))
                        .willReturn(aResponse().withStatus(500).withBody("Simulated fault: " + faultType)));
                break;
            case "DELETE":
                stubFor(delete(urlEqualTo(url))
                        .willReturn(aResponse().withStatus(500).withBody("Simulated fault: " + faultType)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
    
    /**
     * Stub with conditional response based on request body
     */
    public void stubWithRequestBodyMatching(String method, String url, String requestBodyPattern, 
                                          int statusCode, String responseBody) {
        switch (method.toUpperCase()) {
            case "POST":
                stubFor(post(urlEqualTo(url))
                        .withRequestBody(matching(requestBodyPattern))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
                break;
            case "PUT":
                stubFor(put(urlEqualTo(url))
                        .withRequestBody(matching(requestBodyPattern))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
                break;
            default:
                throw new IllegalArgumentException("Request body matching only supported for POST and PUT");
        }
    }
    
    /**
     * Stub with query parameters
     */
    public void stubWithQueryParams(String method, String url, Map<String, String> queryParams, 
                                  int statusCode, String responseBody) {
        switch (method.toUpperCase()) {
            case "GET":
                stubFor(get(urlPathEqualTo(url))
                        .withQueryParams(queryParams.entrySet().stream()
                                .collect(java.util.stream.Collectors.toMap(
                                        Map.Entry::getKey,
                                        entry -> equalTo(entry.getValue())
                                )))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
                break;
            default:
                throw new IllegalArgumentException("Query parameters only supported for GET");
        }
    }
    
    /**
     * Stub with headers
     */
    public void stubWithHeaders(String method, String url, Map<String, String> headers, 
                              int statusCode, String responseBody) {
        switch (method.toUpperCase()) {
            case "GET":
                stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
                break;
            case "POST":
                stubFor(post(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)));
                break;
            default:
                throw new IllegalArgumentException("Headers only supported for GET and POST");
        }
    }
    
    /**
     * Stub with response transformation
     */
    public void stubWithResponseTransformation(String method, String url, int statusCode, 
                                            String responseBody, String transformFunction) {
        switch (method.toUpperCase()) {
            case "GET":
                stubFor(get(urlEqualTo(url))
                        .willReturn(aResponse()
                                .withStatus(statusCode)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody)
                                .withTransformers(transformFunction)));
                break;
            default:
                throw new IllegalArgumentException("Response transformation only supported for GET");
        }
    }
    
    /**
     * Verify that a request was made
     */
    public void verifyRequest(String method, String url) {
        switch (method.toUpperCase()) {
            case "GET":
                verify(getRequestedFor(urlEqualTo(url)));
                break;
            case "POST":
                verify(postRequestedFor(urlEqualTo(url)));
                break;
            case "PUT":
                verify(putRequestedFor(urlEqualTo(url)));
                break;
            case "DELETE":
                verify(deleteRequestedFor(urlEqualTo(url)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
    
    /**
     * Verify request count
     */
    public void verifyRequestCount(String method, String url, int expectedCount) {
        switch (method.toUpperCase()) {
            case "GET":
                verify(expectedCount, getRequestedFor(urlEqualTo(url)));
                break;
            case "POST":
                verify(expectedCount, postRequestedFor(urlEqualTo(url)));
                break;
            case "PUT":
                verify(expectedCount, putRequestedFor(urlEqualTo(url)));
                break;
            case "DELETE":
                verify(expectedCount, deleteRequestedFor(urlEqualTo(url)));
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }
    }
    
    /**
     * Check if server is running
     */
    public boolean isRunning() {
        return wireMockServer != null && wireMockServer.isRunning();
    }
    
    /**
     * Get server port
     */
    public int getPort() {
        return wireMockServer != null ? wireMockServer.port() : -1;
    }
}
