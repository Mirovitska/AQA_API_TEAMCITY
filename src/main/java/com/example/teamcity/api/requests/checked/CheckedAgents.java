package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Agents;
import com.example.teamcity.api.requests.Request;
import com.example.teamcity.api.requests.unchecked.UncheckedAgents;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;

public class CheckedAgents extends Request {
    public CheckedAgents(RequestSpecification spec) {
        super(spec);
    }

    public Agents getAllUnauthorizedAgents() {
        return new UncheckedAgents(spec)
                .getAllUnauthorizedAgents()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Agents.class);
    }

    public Agents getAllAuthorizedAgents(){
        return new UncheckedAgents(spec)
                .getAllAuthorizedAgents()
                .then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract().as(Agents.class);
    }

    public void updateAgentStatus(String name) {
        new UncheckedAgents(spec)
                .updateAgentStatus(name)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
