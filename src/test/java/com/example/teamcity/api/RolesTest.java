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

        //generate test data
        var testData = testDataStorage.addTestData();

        //send to create project request and check
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
        //generate test data
        var testData = testDataStorage.addTestData();

        //set role to project
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.SYSTEM_ADMIN, "g"));

        //create user
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        //login as user and create project
        var project = new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getProject());

        //check that id created project equal to test data's project id
        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void projectAdminShouldHaveRightsToCreateBuildConfigToHisProjectTest() {
        //generate test data
        var testData = testDataStorage.addTestData();

        //create project
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        //set role to project
        testData.getUser().setRoles(TestDataGenerator.generateRoles
                (Role.PROJECT_ADMIN, "p:" + testData.getProject().getId()));

        //create user
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        //create build config to project by user
        var buildConfig = new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(testData.getBuildType());

        softy.assertThat(buildConfig.getId()).isEqualTo(testData.getBuildType().getId());
    }

    @Test
    public void projectAdminShouldNotHaveRightsToCreateBuildConfigToAnotherProjectTest() {
        //generate 2 test data
        var firstTestData = testDataStorage.addTestData();
        var secondTestData = testDataStorage.addTestData();

//        var firstUserRequest = new CheckedRequests(Specifications.getSpec().authSpec(firstTestData.getUser()));
//        var secondUserRequest = new CheckedRequests(Specifications.getSpec().authSpec(secondTestData.getUser()));

        //create 2 projects
        checkedWithSuperUser.getProjectRequest().create(firstTestData.getProject());
        checkedWithSuperUser.getProjectRequest().create(secondTestData.getProject());

        //set role to first project
        firstTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                firstTestData.getProject().getId()));

        //create first user
        checkedWithSuperUser.getUserRequest().create(firstTestData.getUser());

        //set role to second project
        secondTestData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                secondTestData.getProject().getId()));
        //create second user
        checkedWithSuperUser.getUserRequest()
                .create(secondTestData.getUser());
        System.out.println(secondTestData.getUser());

        //second user try to create build config to first project
        new UncheckedBuildConfig(
                Specifications.getSpec().authSpec(secondTestData.getUser()))
                .create(firstTestData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);

    }


}
