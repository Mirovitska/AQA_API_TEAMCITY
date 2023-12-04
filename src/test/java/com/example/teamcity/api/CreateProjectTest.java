package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;

public class CreateProjectTest extends BaseApiTest {

    //Positive cases
    @Test
    public void createProjectWithValidData() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        softy.assertThat(project.getId()).isEqualTo(testData.getProject().getId());
    }

    @Test
    public void createProjectInRootDirectoryAndCheckIt() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var project = checkedWithSuperUser.getProjectRequest().create(testData.getProject());

        softy.assertThat(project.getParentProjectId()).isEqualTo(testData.getProject().getParentProject().getLocator());
    }

    @Test
    public void copiedAllAssociatedSettingsInProject() {
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        checkedWithSuperUser.getProjectRequest().create(testData.getProject());
        softy.assertThat(testData.getProject().getCopyAllAssociatedSettings())
                .isEqualTo(true);
    }

    @Test
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

    @Test
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


    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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

    @Test
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
