package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.specification.Specifications;
import com.example.teamcity.ui.pages.admin.CreateBuildConfig;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;


public class CreateBuildConfigTest extends BaseUiTest {

    @Test
    public void authorizedUserShouldBeAbleCreateBuildConfig() {
        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Mirovitska/Bakery";

        loginAsUser(testData.getUser());

        new CreateBuildConfig()
                .open(testData.getProject().getParentProject().getLocator())
                .setupBuildConfig(url, testData.getUser().getUsername(), testData.getUser().getPassword());

        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getBuildConfig().shouldHave(Condition.text(testData.getProject().getParentProject().getLocator()));
        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .get(testData.getBuildType().getId());
    }

    //negative
    @Test
    public void authorizedUserShouldSeeErrorForInvalidProjectURL() {
        var testData = testDataStorage.addTestData();
        var incorrectUrl = "https://github.com/Mirovitska/Bakery123455";

        new CreateBuildConfig()
                .open(testData.getProject().getParentProject().getLocator())
                .setupBuildConfig(incorrectUrl, testData.getUser().getUsername(), testData.getUser().getPassword())
                .checkError();
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .get(testData.getBuildType().getId())
                .then()
                .statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

}
