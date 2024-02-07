package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.models.User;
import com.example.teamcity.api.requests.checked.CheckedUser;
import com.example.teamcity.api.specification.Specifications;
import com.example.teamcity.ui.pages.AgentPage;
import com.example.teamcity.ui.pages.LoginPage;
import com.example.teamcity.ui.pages.StartUpPage;
import org.testng.annotations.Test;

public class SetupFirstStartTest extends BaseUiTest {
    @Test
    public void setupTeamCityServerTest() {
        StartUpPage.open().setupTeamCityServer();
    }

    @Test
    public void setupTeamCityAgentTest(User user) {
        new CheckedUser(Specifications.getSpec().superUserSpec()).create(user);
        new LoginPage().open().login(user);
        AgentPage.open().authorizeAgent();
    }
}
