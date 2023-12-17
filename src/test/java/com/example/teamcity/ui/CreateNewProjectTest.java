package com.example.teamcity.ui;

import com.codeborne.selenide.Condition;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import com.example.teamcity.ui.pages.admin.CreateNewProject;
import com.example.teamcity.ui.pages.favorites.ProjectsPage;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class CreateNewProjectTest extends BaseUiTest {
    @Test
    public void authorizedUserShouldBeAbleCreateNewProject() {

        var testData = testDataStorage.addTestData();
        var url = "https://github.com/Mirovitska/Bakery";

        loginAsUser(testData.getUser());

        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(url)
                .setupProject(testData.getProject().getName(), testData.getBuildType().getName());
        new ProjectsPage().open()
                .getSubprojects()
                .stream().reduce((first, second) -> second).get()
                .getHeader().shouldHave(Condition.text(testData.getProject().getName()));
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .get(testData.getProject().getId()).then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    //negative
    @Test

    public void authorizedUserShouldSeeErrorForInvalidProjectURL() {
        var testData = testDataStorage.addTestData();
        var incorrectUrl = "https://github.com/Mirovitska/Bakery123455";

        loginAsUser(testData.getUser());
        new CreateNewProject()
                .open(testData.getProject().getParentProject().getLocator())
                .createProjectByUrl(incorrectUrl)
                .checkError();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .get(testData.getProject().getId()).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }
}
