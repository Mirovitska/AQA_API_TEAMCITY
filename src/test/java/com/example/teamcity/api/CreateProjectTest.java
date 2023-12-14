package com.example.teamcity.api;

import com.example.teamcity.api.generators.RandomData;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.requests.checked.CheckedProject;
import com.example.teamcity.api.requests.unchecked.UncheckedProject;
import com.example.teamcity.api.specification.Specifications;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;

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
        var specialSymbolsAndNumbers = RandomData.getStringWithSpecialSymbolsAndNumbers();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(specialSymbolsAndNumbers)
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test
    public void createProjectWithReallyLongOfName() {
        var longName = RandomData.getStringExactCountOfChars(10000);
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(longName)
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }


    @Test
    public void createProjectWithMinIdLength() {
        var oneSymbol = RandomData.getStringExactCountOfChars(1);
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(oneSymbol)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test
    public void createProjectWithMaxIdLength() {
        var maxIdLength = RandomData.getStringExactCountOfChars(222);
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(maxIdLength)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    @Test
    public void createProjectWithoutParentProjectId() {
        var emptyString = "";
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .parentProjectId(emptyString)
                .build();
        new CheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj);
    }

    //Negative cases
    @Test
    public void createProjectWithSpecialSymbolsAndNumbersInID() {
        var stringWithSpecSymbols = RandomData.getStringWithSpecialSymbolsAndNumbers();
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(stringWithSpecSymbols)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ID should start with a latin letter and contain only latin letters, digits and underscores (at most 225 characters)"));
    }

    @Test
    public void createProjectWithEmptyId() {
        var emptyString = "";
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(emptyString)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(containsString("Project ID must not be empty"));
    }

    @Test
    public void createProjectWithNameWhatAlreadyExist() {
        var nameProject = "Test";
        var testData = testDataStorage.addTestData();
        var firstProject = Project.builder()
                .id(RandomData.getString())
                .name(nameProject)
                .build();
        var secondProject = Project.builder()
                .id(RandomData.getString())
                .name(nameProject)
                .build();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(firstProject);
        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(secondProject).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Project with this name already exists"));

    }

    @Test
    public void createProjectWithIdProjectsStartWithNumber() {
        var stringStartsWithNumber = "1" + RandomData.getString();
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(stringStartsWithNumber)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(Matchers.containsString("invalid: starts with non-letter character"));

    }

    @Test
    public void createProjectWithIdProjectsStartWithSpecialSymbol() {
        var stringStartsWithNumber = RandomData.getSpecialSymbol() + RandomData.getString();
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(stringStartsWithNumber)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(containsString("invalid: starts with non-letter character"));
    }

    @Test
    public void createProjectWithIdWithInvalidCharacters() {
        var invalidCharacters = "#$!";
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(invalidCharacters)
                .name(RandomData.getString())
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                .body(containsString("ID should start with a latin letter and contain only latin letters," +
                        " digits and underscores (at most 225 characters)"));
    }

    @Test
    public void createProjectWithEmptyName() {
        var emptyString = "";
        var testData = testDataStorage.addTestData();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        var proj = Project.builder()
                .id(RandomData.getString())
                .name(emptyString)
                .parentProjectId(testData.getProject().getParentProject().getParentProjectId())
                .build();
        new UncheckedProject(Specifications.getSpec()
                .authSpec(testData.getUser()))
                .create(proj).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Project name cannot be empty."));

    }

    @Test
    public void createProjectWithAlreadyExistingId() {
        var testID = "TestID";
        var testData = testDataStorage.addTestData();
        var firstProject = Project.builder()
                .id(testID)
                .name(RandomData.getString())
                .build();
        var secondProject = Project.builder()
                .id(testID)
                .name(RandomData.getString())
                .build();
        checkedWithSuperUser.getUserRequest().create(testData.getUser());
        new CheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(firstProject);
        new UncheckedProject(Specifications.getSpec().authSpec(testData.getUser()))
                .create(secondProject).then().assertThat().statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(containsString("Project ID \"TestID\" is already used by another project"));
    }

}
