package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.core.Endpoints;
import com.hemanth.core.Specs;
import com.hemanth.models.ApiResponse;
import com.hemanth.models.User;
import com.hemanth.util.JsonUtils;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class GetUserTest extends BaseTest {

    @Test
    public void getUserById_200() {

        Response res = requestFactory.get(Endpoints.userById(5));

        // Assert basic validations
        res.then()
                .spec(Specs.success()) // status
                .body(matchesJsonSchemaInClasspath("schemas/getUserSchema.json")); //schema
                // .time(lessThan(2000L)); //SLA check - response time

        // Extract response data using POJO classes
        String responseBody = res.getBody().asString();
        ApiResponse<User> apiResponse = JsonUtils.fromJson(responseBody, 
            new com.fasterxml.jackson.core.type.TypeReference<ApiResponse<User>>() {});
        
        User user = apiResponse.getData();
        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(5));
        assertThat(user.getEmail(), containsString("@reqres.in"));
        assertThat(user.getFirstName(), equalTo("Charles"));
        assertThat(user.getLastName(), equalTo("Morris"));
        assertThat(user.getAvatar(), matchesRegex(".*5-image\\.jpg$"));

        // Business rules
        assertThat(user.getId(), greaterThan(0));
        assertThat(user.getEmail(), endsWith("@reqres.in"));

        // Additional POJO validations
        assertThat(apiResponse.getSupport(), is(notNullValue()));
        assertThat(apiResponse.getSupport().getUrl(), containsString("reqres.in"));
        assertThat(apiResponse.getSupport().getText(), is(not(emptyString())));

        // Test serialization back to JSON
        String serializedUser = JsonUtils.toJson(user);
        assertThat(serializedUser, is(not(emptyString())));
        assertThat(JsonUtils.isValidJson(serializedUser), is(true));

        // Test deserialization back to object
        User deserializedUser = JsonUtils.fromJson(serializedUser, User.class);
        assertThat(deserializedUser, equalTo(user));
    }

    @Test
    public void getUserById_UsingDirectDeserialization() {
        Response res = requestFactory.get(Endpoints.userById(5));
        
        res.then().spec(Specs.success());
        
        // Direct deserialization to User object
        User user = res.jsonPath().getObject("data", User.class);
        
        assertThat(user, is(notNullValue()));
        assertThat(user.getId(), is(5));
        assertThat(user.getEmail(), containsString("@reqres.in"));
    }
}
