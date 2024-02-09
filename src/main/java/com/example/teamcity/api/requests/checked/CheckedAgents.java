package com.example.teamcity.api.requests.checked;

import com.example.teamcity.api.models.Agents;
import com.example.teamcity.api.requests.CrudInterface;
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

    public void updateAgentAuthorization(String name) {
        new UncheckedAgents(spec)
                .updateAgentAuthorization(name)
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }
}
