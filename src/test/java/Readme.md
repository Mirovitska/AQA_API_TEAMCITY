1. Были созданы 2 тестовых класса

- CreateBuildConfigurationTest
- CreateProjectTest

2. Были добавлены модели данных

- Roles
- Role
- Properties
- Property
- Steps
- Step
- Templates

3. Был обновлен файл BuildType и TestDataGenerator соответственно

4. Внесены некоторые незначительные правки в другие пакеты, файлы

5. Были созданы:

* позитивные тесты на создание проекта

- createProjectWithValidData
- createProjectInRootDirectoryAndCheckIt
- copiedAllAssociatedSettings
- createProjectWithSpecialSymbolsAndNumbersInName
- createProjectWithReallyLongOfName
- createProjectWithMinIdLength
- createProjectWithMaxIdLength
- createProjectWithoutParentProjectId

* негативные тесты на создание проекта

- createProjectWithSpecialSymbolsAndNumbersInID
- createProjectWithEmptyId
- createProjectWithNameWhatAlreadyExist
- createProjectWithIdProjectsStartWithNumber
- createProjectWithIdProjectsStartWithSpecialSymbol
- createProjectWithIdWithInvalidCharacters
- createProjectWithEmptyName
- createProjectWithAlreadyExistingId

* позитивные тесты на конфигурацию проекта

- createBuildConfigurationWithTypicalDataWithStepsAndTemplates
- createBuildConfigurationWithoutTemplateAndSteps
- createBuildConfigurationWithTemplatesButWithoutSteps
- createBuildConfigurationWithProjectViewerRole
- createBuildConfigurationWithEmptyProperties
- createBuildConfigurationWithLongNameAndType

* негативные тесты на конфигурацию проекта

- attemptToCreateDuplicateBuildConfiguration
- attemptToCreateBuildConfigurationWithEmptyName
- attemptToCreateBuildConfigurationWithoutProject
- attemptToCreateBuildConfigurationWithInvalidProject

PS:

- Тесты которые падают, я предпологаю что это баг
- Там где выдается 500 ошибка тоже думаю что это баг, но поставила ее как ожидаемый результат
