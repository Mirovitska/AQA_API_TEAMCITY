package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedBuildQueue extends Request implements CrudInterface {

    public UncheckedBuildQueue(RequestSpecification spec) {
        super(spec);
    }
    private static final String BUILD_QUEUE_ENDPOINT = "/app/rest/buildQueue";

    @Override
    public Response create(Object obj) {
        return given().spec(spec).body(obj)
                .post(BUILD_QUEUE_ENDPOINT);
    }

    @Override
    public Object get(String id) {
        return null;
    }

    @Override
    public Object update(Object obj) {
        return null;
    }

    @Override
    public Response delete(String id) {
        return given().spec(spec)
                .delete(BUILD_QUEUE_ENDPOINT + "/id:" + id);
    }
}
