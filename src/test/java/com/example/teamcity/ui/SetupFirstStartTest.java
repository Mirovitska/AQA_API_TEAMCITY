package com.example.teamcity.ui;

import com.example.teamcity.api.requests.checked.CheckedAgents;
import com.example.teamcity.api.specification.Specifications;
import com.example.teamcity.ui.pages.AgentPage;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.awaitility.Awaitility.await;

public class SetupFirstStartTest extends BaseUiTest {
    @Test
    public void setupTeamCityServerTest() {
        StartUpPage.open().setupTeamCityServer();
    }

    @Test
    public void setupTeamCityAgentTestUI() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());
        AgentPage.open().authorizeAgent();
    }

    @Test
    public void setupTeamCityAgentTestAPI() {
        var testData = testDataStorage.addTestData();
        var waiting = await().atMost(Duration.ofMinutes(10)).pollInterval(Duration.ofSeconds(10));
        loginAsUser(testData.getUser());
        var checkedAgents = new CheckedAgents(Specifications.getSpec().authSpec(testData.getUser()));
        var unauthorizedAgents = checkedAgents.getAllUnauthorizedAgents().getAgent();
        var authorizedAgents = checkedAgents.getAllAuthorizedAgents().getAgent();
        waiting.until(() -> !unauthorizedAgents.isEmpty());
        var agentName = unauthorizedAgents.get(0).getName();
        checkedAgents.updateAgentStatus(agentName);
        waiting.until(() -> !authorizedAgents.isEmpty());
        softy.assertThat(authorizedAgents.get(0).getName()).isEqualTo(agentName);
    }
}