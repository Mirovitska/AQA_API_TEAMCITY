package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseApiTest {

    //Positive cases
    @Test(description = "Create project with valid data", groups = {"Regression"})
    public void createProjectWithValidData() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test(description = "Create project in root directory and check it", groups = {"Regression"})
    public void createProjectInRootDirectoryAndCheckIt() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());
    }

    @Test(description = "Copied all associated settings in project", groups = {"Regression"})
    public void copiedAllAssociatedSettingsInProject() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        softy.assertThat(testData.getProject().getCopyAllAssociatedSettings())
                .isEqualTo(true);
    }

    @Test(description = "Create project with special symbols and numbers in name", groups = {"Regression"})
    public void createProjectWithSpecialSymbolsAndNumbersInName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(RandomData.getStringWithSpecialSymbolsAndNumbers())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test(description = "Create project with really long name", groups = {"Regression"})
    public void createProjectWithReallyLongOfName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(RandomData.getStringExactCountOfChars(10000))
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Ignore
    @Test(description = "Create project with minimum ID length", groups = {"Regression"})
    public void createProjectWithMinIdLength() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id("b")
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test(description = "Create project with maximum ID length", groups = {"Regression"})
    public void createProjectWithMaxIdLength() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getStringExactCountOfChars(222))
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test(description = "Create project without parent project ID", groups = {"Regression"})
    public void createProjectWithoutParentProjectId() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .parentProjectId("")
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    //Negative cases

    @Test(description = "Create project with special symbols and numbers in ID", groups = {"Regression"})
    public void createProjectWithSpecialSymbolsAndNumbersInID() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getStringWithSpecialSymbolsAndNumbers())
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Create project with empty ID", groups = {"Regression"})
    public void createProjectWithEmptyId() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id("")
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);

    }

    @Ignore
    @Test(description = "Create project with name that already exists", groups = {"Regression"})
    public void createProjectWithNameWhatAlreadyExist() {
        var testData = testDataStorage.addTestData();
        var firstProject = Project.builder()
                .id(RandomData.getString())
                .name("Test")
                .build();
        var secondProject = Project.builder()
                .id(RandomData.getString())
                .name("Test")
                .build();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(firstProject);
        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(secondProject).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Test(description = "Create project with ID starting with a number", groups = {"Regression"})
    public void createProjectWithIdProjectsStartWithNumber() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id("1" + RandomData.getString())
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Create project with ID starting with a special symbol", groups = {"Regression"})
    public void createProjectWithIdProjectsStartWithSpecialSymbol() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getSpecialSymbol() + RandomData.getString())
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Create project with ID with invalid characters", groups = {"Regression"})
    public void createProjectWithIdWithInvalidCharacters() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id("#$!")
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Create project with empty name", groups = {"Regression"})
    public void createProjectWithEmptyName() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name("")
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @Ignore
    @Test(description = "Create project with already existing ID", groups = {"Regression"})

    public void createProjectWithAlreadyExistingId() {
        var testData = testDataStorage.addTestData();
        var firstProject = Project.builder()
                .id("TestID")
                .name(RandomData.getString())
                .build();
        var secondProject = Project.builder()
                .id("TestID")
                .name(RandomData.getString())
                .build();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(firstProject);
        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(secondProject).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST);
    }

}
