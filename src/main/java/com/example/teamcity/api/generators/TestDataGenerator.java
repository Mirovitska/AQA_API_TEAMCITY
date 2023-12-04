package com.example.teamcity.api.generators;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.NewProjectDescription;
import com.example.teamcity.api.models.Project;
import com.example.teamcity.api.models.Properties;
import com.example.teamcity.api.models.Property;
import com.example.teamcity.api.models.Role;
import com.example.teamcity.api.models.Roles;
import com.example.teamcity.api.models.Step;
import com.example.teamcity.api.models.Steps;
import com.example.teamcity.api.models.Templates;
import com.example.teamcity.api.models.User;

import java.util.Arrays;
import java.util.List;

public class TestDataGenerator {
    public static TestData generate() {
        var user = User.builder()
                .username(RandomData.getString())
                .password(RandomData.getString())
                .email(RandomData.getString() + "@gmail.com")
                .roles(Roles.builder()
                        .role(Arrays.asList(Role.builder()
                                .roleId("SYSTEM_ADMIN")
                                .scope("g")
                                .build()))
                        .build())
                .build();

        var project = NewProjectDescription
                .builder()
                .parentProject(Project.builder()
                        .locator("_Root")
                        .build())
                .name(RandomData.getString())
                .id(RandomData.getString())
                .copyAllAssociatedSettings(true)
                .build();

        var buildType = BuildType.builder()
                .id(RandomData.getString())
                .name(RandomData.getString())
                .project(project)
                .steps(
                        new Steps(
                                List.of(new Step(
                                        "print hello world",
                                        "simpleRunner",
                                        new Properties(
                                                List.of(
                                                        new Property("script.content", "echo 'Hello World!'"),
                                                        new Property("teamcity.step.mode", "default'"),
                                                        new Property("use.custom.script", "true'")
                                                )
                                        )
                                ))
                        )
                )
                .templates(
                        new Templates(0, List.of())
                )
                .build();


        return TestData.builder()
                .user(user)
                .project(project)
                .buildType(buildType)
                .build();
    }

    public static Roles generateRoles(com.example.teamcity.api.enums.Role role, String scope) {
        return Roles.builder().role
                (Arrays.asList(Role.builder().roleId(role.getText())
                        .scope(scope).build())).build();

    }
}
