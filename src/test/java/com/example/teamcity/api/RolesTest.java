package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

public class RolesTest extends BaseApiTest {

    @Test
    public void unautorizedUserShouldNotHaveRightToCreateProject() {

        var testData = testDataStorage.addTestData();

        new UncheckedProject(Specifications.getSpec().unauthSpec())
                .create(testData.getProject())
                .then().assertThat().statusCode(HttpStatus.SC_UNAUTHORIZED)
                .body(Matchers.containsString("Authentication required"));

        //superUser check that project with id doesn't create
        uncheckedWithSuperUser.getProjectRequest()
                .get(testData.getProject().getId())
                .then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND)
                .body(Matchers.containsString("No project found by locator" +
                        " 'count:1,id:" + testData.getProject().getId()));
    }

    @Test
    public void systemAdminShouldHaveRightsToCreateProjectTest() {
        var testData = testDataStorage.addTestData();

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProjectTest() {
        var testData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles
                (Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProjectTest() {
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        firstTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                firstTestData.getProject().getId()));

        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                secondTestData.getProject().getId()));
        checkedWithSuperUser.getUserRequest()
                .create(secondTestData.getUser());
        System.out.println(secondTestData.getUser());

        new UncheckedBuildConfig(
                Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

    }


}
