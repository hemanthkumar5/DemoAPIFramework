package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.core.Endpoints;
import com.hemanth.core.Specs;
import com.hemanth.models.ApiResponse;
import com.hemanth.models.User;
import com.hemanth.util.DataProviderUtils;
import com.hemanth.util.JsonUtils;
import com.hemanth.util.TestDataBuilder;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.sameInstance;

public class CreateUserFromDataProviders extends BaseTest {

    @Test(dataProvider = "csvData", dataProviderClass = DataProviderUtils.class)
    public void createUserFromCSV(String name, String job){
        // Build User object using TestDataBuilder
        User user = TestDataBuilder.buildUserFromCsvData(name, job);
        
        // Serialize User object to JSON
        String body = JsonUtils.toJson(user);

        Response res = requestFactory.post(Endpoints.users(), body);

        res.then().spec(Specs.created());

        // Deserialize response to ApiResponse<User>
        String responseBody = res.getBody().asString();
        ApiResponse<User> apiResponse = JsonUtils.fromJson(responseBody, 
            new com.fasterxml.jackson.core.type.TypeReference<ApiResponse<User>>() {});

        // Validate response using POJO
        assertThat(apiResponse.getName(), equalTo(name));
        assertThat(apiResponse.getJob(), equalTo(job));
        assertThat(apiResponse.getId(), is(notNullValue()));
        assertThat(apiResponse.getCreatedAt(), is(notNullValue()));

        // Additional validations
        assertThat(apiResponse.getId(), is(greaterThan(0)));
        assertThat(apiResponse.getCreatedAt(), is(not(emptyString())));
    }

    @Test
    public void createUserUsingPOJO() {
        // Create User object using TestDataBuilder
        User user = TestDataBuilder.buildRealisticUser();
        
        // Serialize to JSON
        String requestBody = JsonUtils.toJson(user);
        
        Response res = requestFactory.post(Endpoints.users(), requestBody);
        res.then().spec(Specs.created());
        
        // Validate using direct deserialization
        ApiResponse<User> response = res.jsonPath().getObject("", ApiResponse.class);
        
        assertThat(response.getName(), equalTo(user.getName()));
        assertThat(response.getJob(), equalTo(user.getJob()));
        assertThat(response.getId(), is(notNullValue()));
    }

    @Test
    public void createUserWithMinimalData() {
        User user = TestDataBuilder.buildMinimalUser("Minimal User", "Minimal Job");
        
        String requestBody = JsonUtils.toJson(user);
        Response res = requestFactory.post(Endpoints.users(), requestBody);
        
        res.then().spec(Specs.created());
        
        // Test serialization/deserialization cycle
        String serializedUser = JsonUtils.toJson(user);
        User deserializedUser = JsonUtils.fromJson(serializedUser, User.class);
        
        assertThat(deserializedUser.getName(), equalTo(user.getName()));
        assertThat(deserializedUser.getJob(), equalTo(user.getJob()));
    }

    @Test
    public void testUserObjectEquality() {
        User user1 = TestDataBuilder.buildUser("John Doe", "Engineer");
        User user2 = TestDataBuilder.buildUser("John Doe", "Engineer");
        User user3 = TestDataBuilder.buildUser("Jane Doe", "Manager");
        
        // Test equals and hashCode
        assertThat(user1, equalTo(user2));
        assertThat(user1.hashCode(), equalTo(user2.hashCode()));
        assertThat(user1, is(not(equalTo(user3))));
        
        // Test deep copy
        User copiedUser = JsonUtils.deepCopy(user1, User.class);
        assertThat(copiedUser, equalTo(user1));
        assertThat(copiedUser, is(not(sameInstance(user1))));
    }
}
