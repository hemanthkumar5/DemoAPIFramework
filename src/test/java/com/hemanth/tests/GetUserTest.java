package com.hemanth.tests;

import com.hemanth.base.BaseTest;
import com.hemanth.core.Endpoints;
import com.hemanth.core.Specs;
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
                .body(matchesJsonSchemaInClasspath("schemas/getUserSchema.json")) //schema
                .time(lessThan(2000L)); //SLA check - response time

        //Extract response data
        var data = res.jsonPath().getMap("data");

        assertThat(data, is(notNullValue()));
        assertThat(data.get("id"), is(5));
        assertThat(String.valueOf(data.get("email")), containsString("@reqres.in"));
        assertThat(String.valueOf(data.get("first_name")), equalTo("Charles"));
        assertThat(String.valueOf(data.get("last_name")), equalTo("Morris"));
        assertThat(String.valueOf(data.get("avatar")), matchesRegex(".*5-image\\.jpg$"));

        // Business rules
        assertThat((Integer) data.get("id"), greaterThan(0));
        assertThat(String.valueOf(data.get("email")), endsWith("@reqres.in"));

    }
}
