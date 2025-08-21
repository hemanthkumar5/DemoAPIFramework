package com.hemanth.core;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class RequestFactory {
    public Response get(String path) {
        return given().spec(Specs.request()).when().get(path);
    }

    public Response post(String path, Object body) {
        return given().spec(Specs.request()).body(body).when().post(path);
    }

    public Response put(String path, Object body) {
        return given().spec(Specs.request()).body(body).when().put(path);
    }

    public Response delete(String path) {
        return given().spec(Specs.request()).when().delete(path);
    }

}
