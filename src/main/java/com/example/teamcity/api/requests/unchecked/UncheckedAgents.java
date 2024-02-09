package com.example.teamcity.api.requests.unchecked;

import com.example.teamcity.api.requests.CrudInterface;
import com.example.teamcity.api.requests.Request;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class UncheckedAgents extends Request {
    private static final String AGENTS_ENDPOINT = "/app/rest/agents";

    public UncheckedAgents(RequestSpecification spec) {
        super(spec);
    }

    public Response getAllUnauthorizedAgents(){
        return given().spec(spec)
                .get(AGENTS_ENDPOINT + "?locator=enabled:true,authorized:false");
    }
    public Response updateAgentAuthorization(String name){
        return given().spec(spec)
                .contentType("text/plain")
                .accept("text/plain")
                .body(String.valueOf(true))
                .put(AGENTS_ENDPOINT + "/" + name + "/authorized");
    }
}
