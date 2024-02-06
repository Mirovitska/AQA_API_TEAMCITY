package com.example.teamcity.api;

import com.example.teamcity.api.enums.Role;
import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.generators.TestDataGenerator;
import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Properties;
import com.example.teamcity.api.models.Step;
import com.example.teamcity.api.models.Steps;
import com.example.teamcity.api.models.Templates;
import com.example.teamcity.api.requests.checked.CheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedBuildConfig;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

import java.util.List;

public class CreateBuildConfigurationTest extends BaseApiTest {

    //positive

    @Test(description = "Create build configuration with typical data, steps, and templates", groups = {"Regression"})
    public void createBuildConfigurationWithTypicalDataWithStepsAndTemplates() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());

        checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        new UncheckedBuildConfig(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(testData.getBuildType())
                .then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Test(description = "Create build configuration without template and steps", groups = {"Regression"})
    public void createBuildConfigurationWithoutTemplateAndSteps() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .build();
        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);
    }

    @Test(description = "Create build configuration with templates but without steps", groups = {"Regression"})
    public void createBuildConfigurationWithTemplatesButWithoutSteps() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .templates(new Templates(1, List.of()))
                .build();
        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);
    }

    @Test(description = "Create build configuration with project viewer role", groups = {"Regression"})
    public void createBuildConfigurationWithProjectViewerRole() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_VIEWER, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .build();

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);
    }

    @Test(description = "Create build configuration with empty properties", groups = {"Regression"})
    public void createBuildConfigurationWithEmptyProperties() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .steps(new Steps(List.of(new Step(
                        RandomData.getString(),
                        "simple",
                        new Properties()))))
                .build();

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);
    }

    @Test(description = "Create build configuration with long name and type", groups = {"Regression"})
    public void createBuildConfigurationWithLongNameAndType() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .steps(new Steps(List.of(new Step(
                        RandomData.getStringExactCountOfChars(512),
                        RandomData.getStringExactCountOfChars(512),
                        new Properties()))))
                .build();

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);
    }

    //negative
    @Test(description = "Attempt to create duplicate build configuration", groups = {"Regression"})
    public void attemptToCreateDuplicateBuildConfiguration() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));

        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(testData.getProject())
                .build();

        new CheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig);

        var duplicateBuildConfig = BuildType.builder()
                .id(buildConfig.getId())
                .name(RandomData.getString())
                .project(testData.getProject())
                .build();

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(duplicateBuildConfig).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Attempt to create build configuration with empty name", groups = {"Regression"})
    public void attemptToCreateBuildConfigurationWithEmptyName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name("")
                .project(testData.getProject())
                .build();

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Attempt to create build configuration without project", groups = {"Regression"})
    public void attemptToCreateBuildConfigurationWithoutProject() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));
        var buildConfig = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .build();

        new UncheckedBuildConfig(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfig).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Attempt to create build configuration with invalid project", groups = {"Regression"})
    public void attemptToCreateBuildConfigurationWithInvalidProject() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        testData.getUser().setRoles(TestDataGenerator.generateRoles(Role.PROJECT_ADMIN, "p:" +
                testData.getProject().getId()));

        var invalidProject = new NewProjectDescription();
        invalidProject.setId("invalidProjectId");

        var buildConfigWithInvalidProject = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(invalidProject)
                .build();

        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(buildConfigWithInvalidProject).then().assertThat().statusCode(HttpStatus.SC_NOT_FOUND);
    }
}

