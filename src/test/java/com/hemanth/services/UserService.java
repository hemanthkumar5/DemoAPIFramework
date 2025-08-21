package com.hemanth.services;

import com.hemanth.core.RequestFactory;
import com.hemanth.models.User;
import com.hemanth.util.JsonUtils;
import io.restassured.response.Response;

import java.util.Map;

/**
 * Service layer abstraction for User API operations
 */
public class UserService {
    
    private final RequestFactory requestFactory;
    
    public UserService() {
        this.requestFactory = new RequestFactory();
    }
    
    /**
     * Get user by ID
     */
    public Response getUserById(int userId) {
        return requestFactory.get("/api/users/" + userId);
    }
    
    /**
     * Get user by ID with authentication
     */
    public Response getUserByIdAuthenticated(int userId) {
        return requestFactory.getAuthenticated("/api/users/" + userId);
    }
    
    /**
     * Get all users
     */
    public Response getAllUsers() {
        return requestFactory.get("/api/users");
    }
    
    /**
     * Get all users with pagination
     */
    public Response getAllUsers(int page, int perPage) {
        Map<String, String> queryParams = Map.of(
            "page", String.valueOf(page),
            "per_page", String.valueOf(perPage)
        );
        return requestFactory.getWithQueryParams("/api/users", queryParams);
    }
    
    /**
     * Create user
     */
    public Response createUser(User user) {
        return requestFactory.post("/api/users", user);
    }
    
    /**
     * Create user with JSON string
     */
    public Response createUser(String userJson) {
        return requestFactory.post("/api/users", userJson);
    }
    
    /**
     * Create user with Map
     */
    public Response createUser(Map<String, Object> userData) {
        return requestFactory.post("/api/users", userData);
    }
    
    /**
     * Update user
     */
    public Response updateUser(int userId, User user) {
        return requestFactory.put("/api/users/" + userId, user);
    }
    
    /**
     * Update user with JSON string
     */
    public Response updateUser(int userId, String userJson) {
        return requestFactory.put("/api/users/" + userId, userJson);
    }
    
    /**
     * Update user with Map
     */
    public Response updateUser(int userId, Map<String, Object> userData) {
        return requestFactory.put("/api/users/" + userId, userData);
    }
    
    /**
     * Delete user
     */
    public Response deleteUser(int userId) {
        return requestFactory.delete("/api/users/" + userId);
    }
    
    /**
     * Patch user (partial update)
     */
    public Response patchUser(int userId, Map<String, Object> userData) {
        return requestFactory.patch("/api/users/" + userId, userData);
    }
    
    /**
     * Search users by name
     */
    public Response searchUsersByName(String name) {
        Map<String, String> queryParams = Map.of("name", name);
        return requestFactory.getWithQueryParams("/api/users/search", queryParams);
    }
    
    /**
     * Get user profile
     */
    public Response getUserProfile(int userId) {
        return requestFactory.getAuthenticated("/api/users/" + userId + "/profile");
    }
    
    /**
     * Update user profile
     */
    public Response updateUserProfile(int userId, Map<String, Object> profileData) {
        return requestFactory.put("/api/users/" + userId + "/profile", profileData);
    }
    
    /**
     * Get user avatar
     */
    public Response getUserAvatar(int userId) {
        return requestFactory.get("/api/users/" + userId + "/avatar");
    }
    
    /**
     * Upload user avatar
     */
    public Response uploadUserAvatar(int userId, String filePath) {
        // Implementation for file upload would go here
        return null;
    }
    
    /**
     * Get user statistics
     */
    public Response getUserStatistics(int userId) {
        return requestFactory.getAuthenticated("/api/users/" + userId + "/statistics");
    }
    
    /**
     * Get user activity log
     */
    public Response getUserActivityLog(int userId) {
        Map<String, String> queryParams = Map.of("user_id", String.valueOf(userId));
        return requestFactory.getWithQueryParams("/api/users/activity", queryParams);
    }
    
    /**
     * Bulk create users
     */
    public Response bulkCreateUsers(java.util.List<User> users) {
        return requestFactory.post("/api/users/bulk", users);
    }
    
    /**
     * Bulk update users
     */
    public Response bulkUpdateUsers(java.util.List<Map<String, Object>> userUpdates) {
        return requestFactory.put("/api/users/bulk", userUpdates);
    }
    
    /**
     * Bulk delete users
     */
    public Response bulkDeleteUsers(java.util.List<Integer> userIds) {
        return requestFactory.delete("/api/users/bulk");
    }
    
    /**
     * Export users to CSV
     */
    public Response exportUsersToCsv() {
        return requestFactory.get("/api/users/export/csv");
    }
    
    /**
     * Export users to Excel
     */
    public Response exportUsersToExcel() {
        return requestFactory.get("/api/users/export/excel");
    }
    
    /**
     * Import users from CSV
     */
    public Response importUsersFromCsv(String csvContent) {
        return requestFactory.post("/api/users/import/csv", csvContent);
    }
    
    /**
     * Import users from Excel
     */
    public Response importUsersFromExcel(String excelContent) {
        return requestFactory.post("/api/users/import/excel", excelContent);
    }
    
    /**
     * Validate user data
     */
    public boolean validateUserData(User user) {
        return user != null && 
               user.getName() != null && !user.getName().trim().isEmpty() &&
               user.getJob() != null && !user.getJob().trim().isEmpty();
    }
    
    /**
     * Extract user from response
     */
    public User extractUserFromResponse(Response response) {
        String responseBody = response.getBody().asString();
        return JsonUtils.fromJson(responseBody, User.class);
    }
    
    /**
     * Extract users list from response
     */
    public java.util.List<User> extractUsersListFromResponse(Response response) {
        String responseBody = response.getBody().asString();
        return JsonUtils.fromJsonToList(responseBody, User.class);
    }
}
