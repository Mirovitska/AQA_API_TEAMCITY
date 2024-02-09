package com.example.teamcity.ui;

import com.example.teamcity.ui.pages.AgentPage;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

public class SetupFirstStartTest extends BaseUiTest {
    @Test
    public void setupTeamCityServerTest() {
        StartUpPage.open().setupTeamCityServer();
    }

    @Test
    public void setupTeamCityAgentTest() {
        var testData = testDataStorage.addTestData();
        loginAsUser(testData.getUser());
        AgentPage.open().authorizeAgent();
    }

}
